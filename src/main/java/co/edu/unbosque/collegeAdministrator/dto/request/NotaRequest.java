package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Datos para registrar la nota final de una materia.
 *
 * @param nota la nota final, entre 0.0 y 5.0
 */
public record NotaRequest(@NotNull @DecimalMin("0.0") @DecimalMax("5.0") BigDecimal nota) {
}
