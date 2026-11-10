package co.edu.unbosque.collegeAdministrator.dto.response;

/**
 * Datos del presupuesto de horas de un tipo de vinculación en un periodo.
 *
 * @param idPresupuesto el identificador del presupuesto
 * @param idPeriodo el identificador del periodo académico
 * @param idTipoVinculacion el identificador del tipo de vinculación
 * @param tipoVinculacion el nombre del tipo de vinculación
 * @param horasPresupuestadas las horas presupuestadas
 */
public record PresupuestoResponse(Integer idPresupuesto, Integer idPeriodo, Integer idTipoVinculacion,
                                  String tipoVinculacion, Integer horasPresupuestadas) {
}
