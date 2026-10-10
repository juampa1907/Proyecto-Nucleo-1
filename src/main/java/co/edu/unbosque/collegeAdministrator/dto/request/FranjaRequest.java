package co.edu.unbosque.collegeAdministrator.dto.request;

import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

/**
 * Horas en formato "HH:mm" (por ejemplo "07:00").
 */
public record FranjaRequest(@NotNull DiaSemana dia, @NotNull LocalTime horaInicio, @NotNull LocalTime horaFin) {

    /**
     * Lanza IllegalArgumentException (→ 400) si la hora de fin no es posterior a la de inicio.
     */
    public FranjaHoraria aFranja() {
        return new FranjaHoraria(dia, horaInicio, horaFin);
    }
}
