package co.edu.unbosque.collegeAdministrator.dto.request;

import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * @param caracteristicas opcional; al actualizar, reemplaza las características actuales.
 */
public record AulaRequest(
        @NotBlank @Size(max = 20) String codigo,
        @NotNull @Positive Integer capacidad,
        @NotNull TipoEspacio tipoEspacio,
        @NotNull Bloque bloque,
        Set<CaracteristicaAula> caracteristicas) {
}
