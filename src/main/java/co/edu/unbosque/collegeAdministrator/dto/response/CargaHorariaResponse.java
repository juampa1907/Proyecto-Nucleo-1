package co.edu.unbosque.collegeAdministrator.dto.response;


/**
 * Carga horaria de un docente en un periodo.
 *
 * @param idDocente el identificador del docente
 * @param idPeriodo el identificador del periodo académico
 * @param horasMaximas las horas máximas semanales permitidas por la vinculación, o {@code null} si no tiene
 *         vinculación
 * @param horasAsignadas las horas semanales asignadas
 * @param horasDisponibles las horas que aún puede asumir, o {@code null} si no tiene vinculación
 */
public record CargaHorariaResponse(Integer idDocente, Integer idPeriodo, Integer horasMaximas, double horasAsignadas,
                                   Double horasDisponibles) {
}
