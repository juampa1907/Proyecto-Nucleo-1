package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;

/**
 * Datos de una notificación.
 *
 * @param idNotificacion el identificador de la notificación
 * @param asunto el asunto de la notificación
 * @param mensaje el contenido de la notificación
 * @param fecha la fecha y hora de creación
 * @param leida {@code true} si ya fue leída
 */
public record NotificacionResponse(Integer idNotificacion, String asunto, String mensaje, LocalDateTime fecha, boolean leida) {
}
