package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public record PeriodoRequest(
        @NotBlank @Size(max = 20) String codigo,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotNull LocalDate fechaAperturaInscripcion,
        @NotNull LocalDate fechaCierreInscripcion) {
}
