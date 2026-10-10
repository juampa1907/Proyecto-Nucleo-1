package co.edu.unbosque.collegeAdministrator.dto.reporte;

public record ResumenGrupo(Integer idGrupo, String materia, String codigo, String docente,
                           long inscritos, int cupoMaximo) {
}
