package co.edu.unbosque.collegeAdministrator.dto.reporte;

import java.util.List;


public record TablaReporte(String titulo, List<String> columnas, List<List<String>> filas) {
}
