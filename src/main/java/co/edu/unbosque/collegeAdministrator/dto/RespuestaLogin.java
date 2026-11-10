package co.edu.unbosque.collegeAdministrator.dto;

import java.util.List;

/**
 * Respuesta de un inicio de sesión exitoso.
 *
 * @param token el token JWT emitido
 * @param username el nombre del usuario autenticado
 * @param roles los nombres de los roles del usuario
 */
public record RespuestaLogin(String token, String username, List<String> roles) {
}
