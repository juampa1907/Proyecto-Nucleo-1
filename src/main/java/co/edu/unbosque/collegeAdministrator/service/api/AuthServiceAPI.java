package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.RespuestaLogin;


/**
 * Operaciones de autenticación y recuperación de contraseña.
 */
public interface AuthServiceAPI {

    /**
     * Autentica a un usuario y le emite un token.
     *
     * @param username el nombre de usuario
     * @param password la contraseña del usuario
     * @param ip la dirección IP desde la que se inicia sesión
     * @return el token y los datos del usuario autenticado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.AutenticacionException si las credenciales
     *         son inválidas, la cuenta está bloqueada o no tiene roles
     */
    RespuestaLogin login(String username, String password, String ip);


    /**
     * Envía un enlace de recuperación si el correo está registrado; si no lo está, no hace nada para no
     * revelar qué correos existen.
     *
     * @param correo el correo electrónico del usuario
     */
    void solicitarRecuperacion(String correo);

    /**
     * Cambia la contraseña usando un token de recuperación.
     *
     * @param token el token recibido en el enlace
     * @param passwordNueva la nueva contraseña
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el token no es
     *         válido o vigente o la contraseña no cumple las reglas
     */
    void restablecerPassword(String token, String passwordNueva);
}
