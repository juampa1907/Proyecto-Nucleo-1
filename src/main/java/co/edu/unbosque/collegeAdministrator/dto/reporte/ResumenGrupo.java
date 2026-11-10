package co.edu.unbosque.collegeAdministrator.dto.reporte;

/**
 * Resumen de un grupo para los reportes.
 *
 * @param idGrupo el identificador del grupo
 * @param materia el nombre de la materia
 * @param codigo el código del grupo
 * @param docente el nombre del docente, o {@code null} si no tiene
 * @param inscritos la cantidad de estudiantes inscritos
 * @param cupoMaximo el cupo máximo del grupo
 */
public record ResumenGrupo(Integer idGrupo, String materia, String codigo, String docente,
                           long inscritos, int cupoMaximo) {
}
