package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;

import java.time.LocalTime;

/**
 * Datos de una restricción horaria de un docente.
 *
 * @param idRestriccion el identificador de la restricción horaria
 * @param dia el día de la semana
 * @param horaInicio la hora de inicio
 * @param horaFin la hora de fin
 */
public record RestriccionResponse(Integer idRestriccion, DiaSemana dia, LocalTime horaInicio, LocalTime horaFin) {
}
