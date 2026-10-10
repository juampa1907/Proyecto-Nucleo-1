package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;

import java.util.List;

/**
 * HU-30: grupo con horario, docente, aula y cupos disponibles en tiempo real.
 *
 * @param idDocente null si aún no tiene docente.
 */
public record GrupoResponse(Integer idGrupo, String codigo, MateriaResumen materia, Integer idPeriodo, String periodo,
                            Integer idDocente, String docente, Integer cupoMaximo, long cuposDisponibles,
                            EstadoGrupo estado, List<SesionResponse> sesiones) {
}
