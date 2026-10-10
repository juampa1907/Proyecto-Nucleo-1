package co.edu.unbosque.collegeAdministrator.dto.reporte;

import java.util.List;

/**
 * HU-45: forma común de cualquier reporte u horario para exportarlo a PDF o Excel.
 */
public record TablaReporte(String titulo, List<String> columnas, List<List<String>> filas) {
}
