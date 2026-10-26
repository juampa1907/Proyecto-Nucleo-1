package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;

import java.util.List;


public record GrupoResponse(Integer idGrupo, String codigo, MateriaResumen materia, Integer idPeriodo, String periodo,
                            Integer idDocente, String docente, Integer cupoMaximo, long cuposDisponibles,
                            EstadoGrupo estado, List<SesionResponse> sesiones) {
}
