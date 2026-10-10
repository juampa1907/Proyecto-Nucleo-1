package co.edu.unbosque.collegeAdministrator.dto.response;

public record EstudianteResponse(Integer idUsuario, String username, String correo, Integer numeroSemestre,
                                 int creditosAprobados) {
}
