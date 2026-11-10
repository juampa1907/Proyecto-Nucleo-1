package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;

/**
 * Datos de un registro de bitácora.
 *
 * @param idRegistro el identificador del registro de bitácora
 * @param username el nombre del usuario que realizó la acción
 * @param fecha la fecha y hora del registro
 * @param accion el código de la acción
 * @param detalle el detalle de la acción
 */
public record BitacoraResponse(Integer idRegistro, String username, LocalDateTime fecha, String accion, String detalle) {
}
