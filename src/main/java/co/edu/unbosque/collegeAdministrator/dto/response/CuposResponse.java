package co.edu.unbosque.collegeAdministrator.dto.response;


/**
 * Cupos de un grupo.
 *
 * @param idGrupo el identificador del grupo
 * @param cupoMaximo el cupo máximo del grupo
 * @param cuposDisponibles la cantidad de cupos disponibles
 */
public record CuposResponse(Integer idGrupo, Integer cupoMaximo, long cuposDisponibles) {
}
