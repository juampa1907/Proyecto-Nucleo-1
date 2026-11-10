package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.reporte.TablaReporte;


/**
 * Exportación de tablas de reporte a archivos.
 */
public interface ExportacionServiceAPI {

    /**
     * Exporta una tabla a PDF.
     *
     * @param tabla la tabla que se exporta
     * @return el contenido del archivo PDF
     */
    byte[] exportarPdf(TablaReporte tabla);

    /**
     * Exporta una tabla a Excel.
     *
     * @param tabla la tabla que se exporta
     * @return el contenido del archivo xlsx
     */
    byte[] exportarExcel(TablaReporte tabla);
}
