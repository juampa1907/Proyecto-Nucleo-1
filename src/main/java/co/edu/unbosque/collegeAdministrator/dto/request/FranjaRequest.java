package co.edu.unbosque.collegeAdministrator.dto.request;

import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;


public record FranjaRequest(@NotNull DiaSemana dia, @NotNull LocalTime horaInicio, @NotNull LocalTime horaFin) {


    public FranjaHoraria aFranja() {
        return new FranjaHoraria(dia, horaInicio, horaFin);
    }
}
