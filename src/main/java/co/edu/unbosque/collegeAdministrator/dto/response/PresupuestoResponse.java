package co.edu.unbosque.collegeAdministrator.dto.response;

public record PresupuestoResponse(Integer idPresupuesto, Integer idPeriodo, Integer idTipoVinculacion,
                                  String tipoVinculacion, Integer horasPresupuestadas) {
}
