package co.edu.unbosque.collegeAdministrator.dto.response;

/**
 * Datos de un estudiante.
 *
 * @param idUsuario el identificador del usuario
 * @param username el nombre de usuario
 * @param correo el correo electrónico del usuario
 * @param numeroSemestre el semestre que cursa el estudiante
 * @param creditosAprobados los créditos aprobados por el estudiante
 */
public record EstudianteResponse(Integer idUsuario, String username, String correo, Integer numeroSemestre,
                                 int creditosAprobados) {
}
