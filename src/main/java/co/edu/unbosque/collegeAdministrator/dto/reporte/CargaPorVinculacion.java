package co.edu.unbosque.collegeAdministrator.dto.reporte;

/**
 * HU-43: carga asignada por tipo de vinculación frente al presupuesto del periodo.
 *
 * @param horasPresupuestadas null si no se registró presupuesto para ese tipo.
 * @param diferencia          presupuesto − asignadas (negativo = se excede el presupuesto); null sin presupuesto.
 */
public record CargaPorVinculacion(String tipoVinculacion, int docentes, double horasAsignadas,
                                  Integer horasPresupuestadas, Double diferencia) {
}
