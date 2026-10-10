package co.edu.unbosque.collegeAdministrator.dto.response;

/**
 * HU-08: carga de un docente en un periodo.
 *
 * @param horasMaximas      null si el docente no tiene tipo de vinculación.
 * @param horasDisponibles  null si el docente no tiene tipo de vinculación.
 */
public record CargaHorariaResponse(Integer idDocente, Integer idPeriodo, Integer horasMaximas, double horasAsignadas,
                                   Double horasDisponibles) {
}
