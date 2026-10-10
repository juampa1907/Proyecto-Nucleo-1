package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;

public record BitacoraResponse(Integer idRegistro, String username, LocalDateTime fecha, String accion, String detalle) {
}
