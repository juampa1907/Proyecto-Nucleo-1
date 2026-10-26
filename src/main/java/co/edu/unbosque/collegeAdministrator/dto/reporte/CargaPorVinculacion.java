package co.edu.unbosque.collegeAdministrator.dto.reporte;


public record CargaPorVinculacion(String tipoVinculacion, int docentes, double horasAsignadas,
                                  Integer horasPresupuestadas, Double diferencia) {
}
