package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Estudiante;
import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

/**
 * Operaciones de negocio sobre los estudiantes.
 */
public interface EstudianteServiceAPI extends GenericServiceAPI<Estudiante, Integer> {

    /**
     * Busca un estudiante por nombre de usuario.
     *
     * @param username el nombre de usuario
     * @return el estudiante encontrado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si no existe
     */
    Estudiante buscarPorUsername(String username);

    /**
     * Lista las materias que cursa un estudiante.
     *
     * @param idEstudiante el identificador del estudiante
     * @return las materias en curso
     */
    List<Materia> listarMateriasActuales(Integer idEstudiante);

    /**
     * Calcula los créditos aprobados por un estudiante.
     *
     * @param idEstudiante el identificador del estudiante
     * @return los créditos aprobados
     */
    int calcularCreditosAprobados(Integer idEstudiante);
}
