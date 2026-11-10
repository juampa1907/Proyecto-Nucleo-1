package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.RespuestaLogin;
import co.edu.unbosque.collegeAdministrator.dto.request.LoginRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.RecuperarPasswordRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.RestablecerPasswordRequest;
import co.edu.unbosque.collegeAdministrator.service.api.AuthServiceAPI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controlador REST público de autenticación y recuperación de contraseña.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /** El servicio de autenticación y recuperación de contraseña. */
    private final AuthServiceAPI authService;

    /**
     * Crea una instancia de {@code AuthController} con las dependencias requeridas.
     *
     * @param authService el servicio de autenticación y recuperación de contraseña
     */
    public AuthController(AuthServiceAPI authService) {
        this.authService = authService;
    }

    /**
     * Inicia sesión y devuelve un token.
     *
     * <p>Endpoint: {@code POST /auth/login}. Acceso: público.
     *
     * @param request las credenciales
     * @param peticion la solicitud HTTP, de la que se toma la IP
     * @return el token y los datos del usuario
     */
    @PostMapping("/login")
    public RespuestaLogin login(@Valid @RequestBody LoginRequest request, HttpServletRequest peticion) {
        return authService.login(request.username(), request.password(), peticion.getRemoteAddr());
    }


    /**
     * Solicita la recuperación de la contraseña.
     *
     * <p>Endpoint: {@code POST /auth/recuperar-password}. Acceso: público.
     *
     * @param request el correo del usuario
     * @return la respuesta 204, exista o no el correo
     */
    @PostMapping("/recuperar-password")
    public ResponseEntity<Void> recuperarPassword(@Valid @RequestBody RecuperarPasswordRequest request) {
        authService.solicitarRecuperacion(request.correo());
        return ResponseEntity.noContent().build();
    }

    /**
     * Restablece la contraseña con un token.
     *
     * <p>Endpoint: {@code POST /auth/restablecer-password}. Acceso: público.
     *
     * @param request el token y la nueva contraseña
     * @return la respuesta 204
     */
    @PostMapping("/restablecer-password")
    public ResponseEntity<Void> restablecerPassword(@Valid @RequestBody RestablecerPasswordRequest request) {
        authService.restablecerPassword(request.token(), request.passwordNueva());
        return ResponseEntity.noContent().build();
    }
}
