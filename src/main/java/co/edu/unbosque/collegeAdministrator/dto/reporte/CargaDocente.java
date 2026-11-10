package co.edu.unbosque.collegeAdministrator.dto.reporte;


/**
 * Carga horaria de un docente en un periodo.
 *
 * @param idDocente el identificador del docente
 * @param username el nombre de usuario
 * @param tipoVinculacion el nombre del tipo de vinculación
 * @param horasMaximas las horas máximas semanales permitidas por la vinculación, o {@code null} si no tiene
 *         vinculación
 * @param horasAsignadas las horas semanales asignadas
 * @param indicador la clasificación de la carga del docente
 */
public record CargaDocente(Integer idDocente, String username, String tipoVinculacion, Integer horasMaximas,
                           double horasAsignadas, IndicadorCarga indicador) {
}
