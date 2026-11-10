package co.edu.unbosque.collegeAdministrator.dto.reporte;


/**
 * Totales de carga horaria por tipo de vinculación.
 *
 * @param tipoVinculacion el nombre del tipo de vinculación
 * @param docentes la cantidad de docentes con esa vinculación
 * @param horasAsignadas las horas semanales asignadas
 * @param horasPresupuestadas las horas presupuestadas, o {@code null} si no hay presupuesto
 * @param diferencia las horas presupuestadas menos las asignadas, o {@code null} si no hay presupuesto
 */
public record CargaPorVinculacion(String tipoVinculacion, int docentes, double horasAsignadas,
                                  Integer horasPresupuestadas, Double diferencia) {
}
