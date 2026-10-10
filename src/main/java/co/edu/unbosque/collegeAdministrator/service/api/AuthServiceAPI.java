package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.RespuestaLogin;

/**
 * HU-01: inicio de sesión con bloqueo tras 3 intentos. HU-03: recuperación de contraseña por correo.
 */
public interface AuthServiceAPI {

    RespuestaLogin login(String username, String password, String ip);

    /**
     * No informa si el correo existe, para no revelar qué cuentas están registradas.
     */
    void solicitarRecuperacion(String correo);

    void restablecerPassword(String token, String passwordNueva);
}
