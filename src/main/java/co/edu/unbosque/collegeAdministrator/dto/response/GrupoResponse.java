package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;

import java.util.List;


/**
 * Datos de un grupo.
 *
 * @param idGrupo el identificador del grupo
 * @param codigo el código del grupo
 * @param materia el resumen de la materia
 * @param idPeriodo el identificador del periodo académico
 * @param periodo el código del periodo académico
 * @param idDocente el identificador del docente, o {@code null} si no tiene
 * @param docente el nombre del docente, o {@code null} si no tiene
 * @param cupoMaximo el cupo máximo del grupo
 * @param cuposDisponibles la cantidad de cupos disponibles
 * @param estado el estado del grupo
 * @param sesiones las sesiones programadas
 */
public record GrupoResponse(Integer idGrupo, String codigo, MateriaResumen materia, Integer idPeriodo, String periodo,
                            Integer idDocente, String docente, Integer cupoMaximo, long cuposDisponibles,
                            EstadoGrupo estado, List<SesionResponse> sesiones) {
}
