package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;
import java.util.Map;


/**
 * Cuerpo estándar de las respuestas de error.
 *
 * @param fecha la fecha y hora del error
 * @param estado el código de estado HTTP
 * @param error la descripción del estado HTTP
 * @param mensaje el detalle del error
 * @param ruta la ruta solicitada
 * @param erroresCampos los errores por campo, o {@code null} si no aplica
 */
public record RespuestaError(LocalDateTime fecha, int estado, String error, String mensaje, String ruta,
                             Map<String, String> erroresCampos) {
}
