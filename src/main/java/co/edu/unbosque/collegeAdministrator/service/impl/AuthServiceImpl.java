package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.dto.RespuestaLogin;
import co.edu.unbosque.collegeAdministrator.entity.TokenRecuperacion;
import co.edu.unbosque.collegeAdministrator.entity.Usuario;
import co.edu.unbosque.collegeAdministrator.repository.TokenRecuperacionRepository;
import co.edu.unbosque.collegeAdministrator.repository.UsuarioRepository;
import co.edu.unbosque.collegeAdministrator.security.JwtService;
import co.edu.unbosque.collegeAdministrator.service.api.AuthServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.NotificacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.UsuarioServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.exception.AutenticacionException;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementación de la autenticación y la recuperación de contraseña.
 */
@Service
public class AuthServiceImpl implements AuthServiceAPI {

    /** Mensaje genérico para credenciales incorrectas. */
    private static final String CREDENCIALES_INVALIDAS = "Usuario o contraseña incorrectos";

    /** El componente de Spring Security que valida las credenciales. */
    private final AuthenticationManager authenticationManager;
    /** El servicio que genera y lee los tokens JWT. */
    private final JwtService jwtService;
    /** El servicio de gestión de usuarios. */
    private final UsuarioServiceAPI usuarioService;
    /** El repositorio de persistencia de usuarios. */
    private final UsuarioRepository usuarioRepository;
    /** El repositorio de tokens de recuperación de contraseña. */
    private final TokenRecuperacionRepository tokenRecuperacionRepository;
    /** El codificador usado para cifrar y comparar contraseñas. */
    private final PasswordEncoder passwordEncoder;
    /** El servicio de notificaciones a usuarios. */
    private final NotificacionServiceAPI notificacionService;
    /** La URL base de la aplicación web cliente. */
    private final String urlFrontend;

    /**
     * Crea una instancia de {@code AuthServiceImpl} con las dependencias requeridas.
     *
     * @param authenticationManager el componente de Spring Security que valida las credenciales
     * @param jwtService el servicio que genera y lee los tokens JWT
     * @param usuarioService el servicio de gestión de usuarios
     * @param usuarioRepository el repositorio de persistencia de usuarios
     * @param tokenRecuperacionRepository el repositorio de tokens de recuperación de contraseña
     * @param passwordEncoder el codificador usado para cifrar y comparar contraseñas
     * @param notificacionService el servicio de notificaciones a usuarios
     * @param urlFrontend la URL base de la aplicación web cliente
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           UsuarioServiceAPI usuarioService,
                           UsuarioRepository usuarioRepository,
                           TokenRecuperacionRepository tokenRecuperacionRepository,
                           PasswordEncoder passwordEncoder,
                           NotificacionServiceAPI notificacionService,
                           @Value("${app.url-frontend}") String urlFrontend) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.tokenRecuperacionRepository = tokenRecuperacionRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificacionService = notificacionService;
        this.urlFrontend = urlFrontend;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Las excepciones de autenticación no revierten la transacción, para conservar el registro de intentos
     * fallidos.
     */
    @Override
    @Transactional(noRollbackFor = AutenticacionException.class)
    public RespuestaLogin login(String username, String password, String ip) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new AutenticacionException(CREDENCIALES_INVALIDAS));
        if (usuarioService.estaBloqueado(usuario)) {
            throw new AutenticacionException("Cuenta bloqueada temporalmente hasta " + usuario.getBloqueadoHasta());
        }
        if (usuario.getRoles().isEmpty()) {
            throw new AutenticacionException("El usuario no tiene un rol asignado");
        }

        Authentication autenticacion;
        try {
            autenticacion = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            usuarioService.registrarIntentoFallido(username);
            throw new AutenticacionException(CREDENCIALES_INVALIDAS);
        } catch (AuthenticationException e) {
            throw new AutenticacionException("No fue posible iniciar sesión: " + e.getMessage());
        }

        usuarioService.registrarInicioSesion(username, ip);
        UserDetails detalles = (UserDetails) autenticacion.getPrincipal();
        List<String> roles = usuario.getRoles().stream().map(Enum::name).sorted().toList();
        return new RespuestaLogin(jwtService.generarToken(detalles), username, roles);
    }

    /**
     * {@inheritDoc}
     *
     * <p>El enlace solo se envía por correo; la notificación guardada no lo incluye para no exponerlo.
     */
    @Override
    @Transactional
    public void solicitarRecuperacion(String correo) {
        usuarioRepository.findByCorreo(correo).ifPresent(usuario -> {
            String token = UUID.randomUUID().toString();
            tokenRecuperacionRepository.save(new TokenRecuperacion(token, usuario, LocalDateTime.now()));
            String enlace = urlFrontend + "/restablecer-password?token=" + token;
            // El enlace solo va por correo: guardarlo en la tabla de notificaciones lo dejaría expuesto.
            notificacionService.enviarSoloCorreo(usuario, "Recuperación de contraseña",
                    "Se solicitó restablecer tu contraseña. El enlace vence en "
                            + TokenRecuperacion.MINUTOS_VIGENCIA + " minutos:\n" + enlace
                            + "\nSi no fuiste tú, ignora este mensaje.");
            notificacionService.notificar(usuario, "Solicitud de cambio de contraseña",
                    "Se recibió una solicitud para restablecer tu contraseña el " + LocalDateTime.now() + ".");
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void restablecerPassword(String token, String passwordNueva) {
        TokenRecuperacion recuperacion = tokenRecuperacionRepository.findByToken(token)
                .orElseThrow(() -> new ReglaNegocioException("El enlace de recuperación no es válido"));
        if (!recuperacion.estaVigente(LocalDateTime.now())) {
            throw new ReglaNegocioException("El enlace de recuperación expiró o ya fue usado");
        }
        usuarioService.validarPassword(passwordNueva);
        Usuario usuario = recuperacion.getUsuario();
        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        recuperacion.setUsado(true);
        notificacionService.notificar(usuario, "Contraseña actualizada",
                "Tu contraseña se cambió el " + LocalDateTime.now() + ". Si no fuiste tú, contacta al administrador.");
    }
}
