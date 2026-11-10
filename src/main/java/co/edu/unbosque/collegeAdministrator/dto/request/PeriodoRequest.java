package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


/**
 * Datos para crear o actualizar un periodo académico.
 *
 * @param codigo el código del periodo
 * @param fechaInicio la fecha de inicio del periodo
 * @param fechaFin la fecha de fin del periodo
 * @param fechaAperturaInscripcion la fecha de apertura de la inscripción
 * @param fechaCierreInscripcion la fecha de cierre de la inscripción
 */
public record PeriodoRequest(
        @NotBlank @Size(max = 20) String codigo,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotNull LocalDate fechaAperturaInscripcion,
        @NotNull LocalDate fechaCierreInscripcion) {
}
