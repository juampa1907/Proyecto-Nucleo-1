package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;

/**
 * Datos de una versión del horario.
 *
 * @param idVersion el identificador de la versión del horario
 * @param numero el número consecutivo de la versión
 * @param fecha la fecha y hora de creación
 * @param usuario el nombre del usuario que la generó
 * @param descripcion la descripción de la versión
 */
public record VersionHorarioResponse(Integer idVersion, Integer numero, LocalDateTime fecha, String usuario,
                                     String descripcion) {
}
