package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoObservacion;

import java.time.LocalDateTime;

public record ObservacionResponse(Integer idObservacion, Integer idGrupo, String grupo, Integer idDocente, String docente,
                                  String texto, LocalDateTime fecha, EstadoObservacion estado) {
}
