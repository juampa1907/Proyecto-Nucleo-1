package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.Usuario;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import co.edu.unbosque.collegeAdministrator.repository.UsuarioRepository;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.UsuarioServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementación de las operaciones sobre usuarios, roles y credenciales.
 */
@Service
@Transactional
public class UsuarioServiceImpl extends GenericServiceImpl<Usuario, Integer> implements UsuarioServiceAPI {

    /** Intentos fallidos permitidos antes del bloqueo temporal. */
    static final int MAX_INTENTOS = 3;
    /** Duración en minutos del bloqueo temporal. */
    static final int MINUTOS_BLOQUEO = 15;

    /** Longitud mínima de la contraseña. */
    static final int LONGITUD_MINIMA_PASSWORD = 8;

    /** El repositorio de persistencia de usuarios. */
    private final UsuarioRepository usuarioRepository;
    /** El codificador usado para cifrar y comparar contraseñas. */
    private final PasswordEncoder passwordEncoder;
    /** El servicio de auditoría donde se registran las acciones. */
    private final BitacoraServiceAPI bitacoraService;

    /**
     * Crea una instancia de {@code UsuarioServiceImpl} con las dependencias requeridas.
     *
     * @param usuarioRepository el repositorio de persistencia de usuarios
     * @param passwordEncoder el codificador usado para cifrar y comparar contraseñas
     * @param bitacoraService el servicio de auditoría donde se registran las acciones
     */
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder,
                              BitacoraServiceAPI bitacoraService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.bitacoraService = bitacoraService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<Usuario, Integer> getRepository() {
        return usuarioRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Al crear un usuario valida sus datos y cifra su contraseña.
     */
    @Override
    public Usuario guardar(Usuario usuario) {
        if (usuario.getIdUsuario() == null) {
            prepararNuevoUsuario(usuario);
        }
        return super.guardar(usuario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepararNuevoUsuario(Usuario usuario) {
        validarDatosUnicos(usuario.getUsername(), usuario.getCorreo());
        validarPassword(usuario.getPassword());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario crearAdministrador(String username, String correo, String password) {
        Usuario administrador = new Usuario(username, correo, password);
        administrador.agregarRol(Rol.ADMINISTRADOR);
        Usuario guardado = guardar(administrador);
        bitacoraService.registrar("CREAR_ADMINISTRADOR", username);
        return guardado;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario asignarRol(Integer idUsuario, Rol rol) {
        Usuario usuario = buscarPorId(idUsuario);
        usuario.agregarRol(rol);
        bitacoraService.registrar("ASIGNAR_ROL", rol + " a " + usuario.getUsername());
        return usuario;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario quitarRol(Integer idUsuario, Rol rol) {
        Usuario usuario = buscarPorId(idUsuario);
        usuario.quitarRol(rol);
        bitacoraService.registrar("QUITAR_ROL", rol + " a " + usuario.getUsername());
        return usuario;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario cambiarPassword(Integer idUsuario, String passwordActual, String passwordNueva) {
        Usuario usuario = buscarPorId(idUsuario);
        if (passwordActual == null || !passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            throw new ReglaNegocioException("La contraseña actual no es correcta");
        }
        validarPassword(passwordNueva);
        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        return usuario;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validarPassword(String password) {
        if (password == null || password.length() < LONGITUD_MINIMA_PASSWORD) {
            throw new ReglaNegocioException("La contraseña debe tener al menos " + LONGITUD_MINIMA_PASSWORD + " caracteres");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario " + username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public void validarDatosUnicos(String username, String correo) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new ReglaNegocioException("El username " + username + " ya está registrado");
        }
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ReglaNegocioException("El correo " + correo + " ya está registrado");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario registrarInicioSesion(String username, String ip) {
        Usuario usuario = buscarPorUsername(username);
        if (estaBloqueado(usuario)) {
            throw new ReglaNegocioException("El usuario está bloqueado hasta " + usuario.getBloqueadoHasta());
        }
        usuario.setIntentos(0);
        usuario.setBloqueadoHasta(null);
        usuario.setFechaInitSesion(LocalDateTime.now());
        usuario.setIpUltimoInicioSesion(ip);
        return usuario;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Usuario registrarIntentoFallido(String username) {
        Usuario usuario = buscarPorUsername(username);
        int intentos = usuario.getIntentos() + 1;
        if (intentos >= MAX_INTENTOS) {
            usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO));
            usuario.setIntentos(0);
        } else {
            usuario.setIntentos(intentos);
        }
        return usuario;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean estaBloqueado(Usuario usuario) {
        return usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(LocalDateTime.now());
    }
}
