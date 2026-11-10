package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o actualizar un tipo de vinculación.
 *
 * @param nombre el nombre del tipo de vinculación
 * @param horasMaximasSemanales las horas máximas semanales de la vinculación
 */
public record TipoVinculacionRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotNull @Positive Integer horasMaximasSemanales) {
}
