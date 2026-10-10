package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PresupuestoRequest(@NotNull Integer idTipoVinculacion, @NotNull @PositiveOrZero Integer horasPresupuestadas) {
}
