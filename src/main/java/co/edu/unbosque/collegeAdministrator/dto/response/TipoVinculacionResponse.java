package co.edu.unbosque.collegeAdministrator.dto.response;

/**
 * Datos de un tipo de vinculación.
 *
 * @param idTipoVinculacion el identificador del tipo de vinculación
 * @param nombre el nombre del tipo de vinculación
 * @param horasMaximasSemanales las horas máximas semanales de la vinculación
 */
public record TipoVinculacionResponse(Integer idTipoVinculacion, String nombre, Integer horasMaximasSemanales) {
}
