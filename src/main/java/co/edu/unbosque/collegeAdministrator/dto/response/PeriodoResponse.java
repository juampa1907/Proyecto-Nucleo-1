package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoHorario;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoPeriodo;

import java.time.LocalDate;

/**
 * Datos de un periodo académico.
 *
 * @param idPeriodo el identificador del periodo académico
 * @param codigo el código del periodo
 * @param fechaInicio la fecha de inicio del periodo
 * @param fechaFin la fecha de fin del periodo
 * @param fechaAperturaInscripcion la fecha de apertura de la inscripción
 * @param fechaCierreInscripcion la fecha de cierre de la inscripción
 * @param estado el estado del periodo
 * @param estadoHorario el estado del horario del periodo
 */
public record PeriodoResponse(Integer idPeriodo, String codigo, LocalDate fechaInicio, LocalDate fechaFin,
                              LocalDate fechaAperturaInscripcion, LocalDate fechaCierreInscripcion,
                              EstadoPeriodo estado, EstadoHorario estadoHorario) {
}
