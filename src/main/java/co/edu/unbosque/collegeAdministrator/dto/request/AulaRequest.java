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
 * Datos para crear o actualizar un aula.
 *
 * @param codigo el código del aula
 * @param capacidad la capacidad de estudiantes del aula
 * @param tipoEspacio el tipo de espacio del aula
 * @param bloque el bloque del aula
 * @param caracteristicas las características físicas del aula
 */
public record AulaRequest(
        @NotBlank @Size(max = 20) String codigo,
        @NotNull @Positive Integer capacidad,
        @NotNull TipoEspacio tipoEspacio,
        @NotNull Bloque bloque,
        Set<CaracteristicaAula> caracteristicas) {
}
