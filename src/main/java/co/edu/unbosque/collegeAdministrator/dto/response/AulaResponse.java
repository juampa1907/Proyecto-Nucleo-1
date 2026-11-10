package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;

import java.util.Set;

/**
 * Datos de un aula.
 *
 * @param idAula el identificador del aula
 * @param codigo el código del aula
 * @param capacidad la capacidad de estudiantes del aula
 * @param tipoEspacio el tipo de espacio del aula
 * @param bloque el bloque o edificio
 * @param sede la sede en la que se ubica el bloque
 * @param caracteristicas las características físicas del aula
 * @param activa {@code true} si el aula está activa
 */
public record AulaResponse(Integer idAula, String codigo, Integer capacidad, TipoEspacio tipoEspacio, Bloque bloque,
                           Sede sede, Set<CaracteristicaAula> caracteristicas, boolean activa) {
}
