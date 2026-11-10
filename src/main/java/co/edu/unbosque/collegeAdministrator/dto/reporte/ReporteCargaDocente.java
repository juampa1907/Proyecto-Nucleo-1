package co.edu.unbosque.collegeAdministrator.dto.reporte;

import java.util.List;

/**
 * Reporte de carga horaria docente de un periodo.
 *
 * @param docentes la carga de cada docente
 * @param porVinculacion los totales por tipo de vinculación
 */
public record ReporteCargaDocente(List<CargaDocente> docentes, List<CargaPorVinculacion> porVinculacion) {
}
