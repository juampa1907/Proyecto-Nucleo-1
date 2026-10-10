package co.edu.unbosque.collegeAdministrator.dto.request;

import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record LimiteInventarioRequest(@NotNull TipoEspacio tipoEspacio, @NotNull Sede sede, @NotNull @PositiveOrZero Integer maximo) {
}
