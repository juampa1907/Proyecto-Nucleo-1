package co.edu.unbosque.collegeAdministrator.dto.response;

/**
 * HU-30 / HU-34: cupos del grupo en tiempo real.
 */
public record CuposResponse(Integer idGrupo, Integer cupoMaximo, long cuposDisponibles) {
}
