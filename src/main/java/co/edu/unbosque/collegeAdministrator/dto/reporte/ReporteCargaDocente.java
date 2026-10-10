package co.edu.unbosque.collegeAdministrator.dto.reporte;

import java.util.List;

public record ReporteCargaDocente(List<CargaDocente> docentes, List<CargaPorVinculacion> porVinculacion) {
}
