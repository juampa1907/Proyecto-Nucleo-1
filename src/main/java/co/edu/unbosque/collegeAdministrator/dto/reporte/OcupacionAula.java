package co.edu.unbosque.collegeAdministrator.dto.reporte;

import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;


/**
 * Ocupación de un aula en un periodo.
 *
 * @param idAula el identificador del aula
 * @param codigo el código del aula
 * @param bloque el bloque del aula
 * @param tipoEspacio el tipo de espacio del aula
 * @param sesiones la cantidad de sesiones consideradas
 * @param horasOcupadas las horas ocupadas dentro de la ventana consultada
 * @param porcentajeOcupacion el porcentaje de ocupación sobre las horas disponibles
 */
public record OcupacionAula(Integer idAula, String codigo, Bloque bloque, TipoEspacio tipoEspacio,
                            int sesiones, double horasOcupadas, double porcentajeOcupacion) {
}
