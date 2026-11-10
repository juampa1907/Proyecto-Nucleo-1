package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;

/**
 * Datos del tope de aulas de un tipo de espacio en una sede.
 *
 * @param idLimite el identificador del tope de inventario
 * @param tipoEspacio el tipo de espacio del aula
 * @param sede la sede de la universidad
 * @param maximo el número máximo de aulas
 */
public record LimiteInventarioResponse(Integer idLimite, TipoEspacio tipoEspacio, Sede sede, Integer maximo) {
}
