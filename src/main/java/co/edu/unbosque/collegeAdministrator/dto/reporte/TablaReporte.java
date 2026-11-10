package co.edu.unbosque.collegeAdministrator.dto.reporte;

import java.util.List;


/**
 * Reporte en formato tabular, listo para mostrarse o exportarse.
 *
 * @param titulo el título del reporte
 * @param columnas los encabezados de las columnas
 * @param filas los valores de cada fila
 */
public record TablaReporte(String titulo, List<String> columnas, List<List<String>> filas) {
}
