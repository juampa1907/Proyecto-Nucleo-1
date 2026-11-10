package co.edu.unbosque.collegeAdministrator.dto.request;

import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Datos para definir el tope de aulas de un tipo de espacio en una sede.
 *
 * @param tipoEspacio el tipo de espacio del aula
 * @param sede la sede de la universidad
 * @param maximo el número máximo de aulas
 */
public record LimiteInventarioRequest(@NotNull TipoEspacio tipoEspacio, @NotNull Sede sede, @NotNull @PositiveOrZero Integer maximo) {
}
