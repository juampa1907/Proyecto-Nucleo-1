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

@Service
@Transactional
public class UsuarioServiceImpl extends GenericServiceImpl<Usuario, Integer> implements UsuarioServiceAPI {

    static final int MAX_INTENTOS = 3;
    static final int MINUTOS_BLOQUEO = 15;

    static final int LONGITUD_MINIMA_PASSWORD = 8;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final BitacoraServiceAPI bitacoraService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder,
                              BitacoraServiceAPI bitacoraService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.bitacoraService = bitacoraService;
    }

    @Override
    protected JpaRepository<Usuario, Integer> getRepository() {
        return usuarioRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        if (usuario.getIdUsuario() == null) {
            prepararNuevoUsuario(usuario);
        }
        return super.guardar(usuario);
    }

    @Override
    public void prepararNuevoUsuario(Usuario usuario) {
        validarDatosUnicos(usuario.getUsername(), usuario.getCorreo());
        validarPassword(usuario.getPassword());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    }

    @Override
    public Usuario crearAdministrador(String username, String correo, String password) {
        Usuario administrador = new Usuario(username, correo, password);
        administrador.agregarRol(Rol.ADMINISTRADOR);
        Usuario guardado = guardar(administrador);
        bitacoraService.registrar("CREAR_ADMINISTRADOR", username);
        return guardado;
    }

    // HU-02: el administrador asigna roles como DIRECTOR_PROGRAMA o DECANO a un usuario existente.
    @Override
    public Usuario asignarRol(Integer idUsuario, Rol rol) {
        Usuario usuario = buscarPorId(idUsuario);
        usuario.agregarRol(rol);
        bitacoraService.registrar("ASIGNAR_ROL", rol + " a " + usuario.getUsername());
        return usuario;
    }

    @Override
    public Usuario quitarRol(Integer idUsuario, Rol rol) {
        Usuario usuario = buscarPorId(idUsuario);
        usuario.quitarRol(rol);
        bitacoraService.registrar("QUITAR_ROL", rol + " a " + usuario.getUsername());
        return usuario;
    }

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

    @Override
    public void validarPassword(String password) {
        if (password == null || password.length() < LONGITUD_MINIMA_PASSWORD) {
            throw new ReglaNegocioException("La contraseña debe tener al menos " + LONGITUD_MINIMA_PASSWORD + " caracteres");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario " + username));
    }

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

    @Override
    public boolean estaBloqueado(Usuario usuario) {
        return usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(LocalDateTime.now());
    }
}
