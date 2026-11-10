package co.edu.unbosque.collegeAdministrator.dto.request;

import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;


/**
 * Datos de una franja horaria.
 *
 * @param dia el día de la semana
 * @param horaInicio la hora de inicio
 * @param horaFin la hora de fin
 */
public record FranjaRequest(@NotNull DiaSemana dia, @NotNull LocalTime horaInicio, @NotNull LocalTime horaFin) {


    /**
     * Convierte los datos recibidos en una franja validada.
     *
     * @return la franja horaria correspondiente
     * @throws IllegalArgumentException si la hora de fin no es posterior a la de inicio
     */
    public FranjaHoraria aFranja() {
        return new FranjaHoraria(dia, horaInicio, horaFin);
    }
}
