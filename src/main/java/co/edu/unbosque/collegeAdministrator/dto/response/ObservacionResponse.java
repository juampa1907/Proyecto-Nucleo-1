package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoObservacion;

import java.time.LocalDateTime;

/**
 * Datos de una observación de asignación.
 *
 * @param idObservacion el identificador de la observación
 * @param idGrupo el identificador del grupo
 * @param grupo la descripción del grupo
 * @param idDocente el identificador del docente
 * @param docente el nombre del docente
 * @param texto el texto de la observación
 * @param fecha la fecha y hora de registro
 * @param estado el estado de la observación
 */
public record ObservacionResponse(Integer idObservacion, Integer idGrupo, String grupo, Integer idDocente, String docente,
                                  String texto, LocalDateTime fecha, EstadoObservacion estado) {
}
