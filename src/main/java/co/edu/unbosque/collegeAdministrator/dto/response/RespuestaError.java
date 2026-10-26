package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;
import java.util.Map;


public record RespuestaError(LocalDateTime fecha, int estado, String error, String mensaje, String ruta,
                             Map<String, String> erroresCampos) {
}
