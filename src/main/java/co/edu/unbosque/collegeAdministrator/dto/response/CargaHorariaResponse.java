package co.edu.unbosque.collegeAdministrator.dto.response;


public record CargaHorariaResponse(Integer idDocente, Integer idPeriodo, Integer horasMaximas, double horasAsignadas,
                                   Double horasDisponibles) {
}
