package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoHorario;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoPeriodo;

import java.time.LocalDate;

public record PeriodoResponse(Integer idPeriodo, String codigo, LocalDate fechaInicio, LocalDate fechaFin,
                              LocalDate fechaAperturaInscripcion, LocalDate fechaCierreInscripcion,
                              EstadoPeriodo estado, EstadoHorario estadoHorario) {
}
