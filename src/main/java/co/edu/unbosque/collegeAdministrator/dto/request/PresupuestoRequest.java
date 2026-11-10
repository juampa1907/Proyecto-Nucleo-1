package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Datos para registrar el presupuesto de horas de un tipo de vinculación.
 *
 * @param idTipoVinculacion el identificador del tipo de vinculación
 * @param horasPresupuestadas las horas presupuestadas
 */
public record PresupuestoRequest(@NotNull Integer idTipoVinculacion, @NotNull @PositiveOrZero Integer horasPresupuestadas) {
}
