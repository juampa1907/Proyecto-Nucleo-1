package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato común de todos los errores de la API.
 *
 * @param erroresCampos solo en errores de validación: campo → mensaje.
 */
public record RespuestaError(LocalDateTime fecha, int estado, String error, String mensaje, String ruta,
                             Map<String, String> erroresCampos) {
}
