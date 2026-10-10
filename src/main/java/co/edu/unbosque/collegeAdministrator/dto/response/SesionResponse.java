package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;

import java.time.LocalTime;

/**
 * @param idAula null si la sesión aún no tiene aula.
 */
public record SesionResponse(Integer idSesion, DiaSemana dia, LocalTime horaInicio, LocalTime horaFin,
                             Integer idAula, String aula, Bloque bloque, boolean usoExcepcional) {
}
