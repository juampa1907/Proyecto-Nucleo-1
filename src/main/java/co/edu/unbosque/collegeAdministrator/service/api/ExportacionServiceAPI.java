package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.reporte.TablaReporte;

/**
 * HU-45: exporta cualquier reporte u horario. Los controladores aplican los filtros antes de construir la tabla,
 * así el archivo conserva lo que el usuario ve en pantalla.
 */
public interface ExportacionServiceAPI {

    byte[] exportarPdf(TablaReporte tabla);

    byte[] exportarExcel(TablaReporte tabla);
}
