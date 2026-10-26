package co.edu.unbosque.collegeAdministrator.dto.reporte;


public record CargaDocente(Integer idDocente, String username, String tipoVinculacion, Integer horasMaximas,
                           double horasAsignadas, IndicadorCarga indicador) {
}
