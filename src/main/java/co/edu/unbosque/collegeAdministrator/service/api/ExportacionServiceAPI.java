package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.reporte.TablaReporte;


public interface ExportacionServiceAPI {

    byte[] exportarPdf(TablaReporte tabla);

    byte[] exportarExcel(TablaReporte tabla);
}
