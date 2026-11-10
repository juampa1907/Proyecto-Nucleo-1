package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;

import java.time.LocalTime;

/**
 * Datos de una sesión de un grupo.
 *
 * @param idSesion el identificador de la sesión
 * @param dia el día de la semana
 * @param horaInicio la hora de inicio
 * @param horaFin la hora de fin
 * @param idAula el identificador del aula, o {@code null} si no tiene
 * @param aula el código del aula, o {@code null} si no tiene
 * @param bloque el bloque del aula, o {@code null} si no tiene
 * @param usoExcepcional {@code true} si el aula se asigna de forma excepcional (requerido para auditorios)
 */
public record SesionResponse(Integer idSesion, DiaSemana dia, LocalTime horaInicio, LocalTime horaFin,
                             Integer idAula, String aula, Bloque bloque, boolean usoExcepcional) {
}
