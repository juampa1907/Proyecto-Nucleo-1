package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;

public record NotificacionResponse(Integer idNotificacion, String asunto, String mensaje, LocalDateTime fecha, boolean leida) {
}
