package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

/**
 * Operaciones de negocio sobre los docentes.
 */
public interface DocenteServiceAPI extends GenericServiceAPI<Docente, Integer> {

    /**
     * Busca un docente por nombre de usuario.
     *
     * @param username el nombre de usuario
     * @return el docente encontrado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si no existe
     */
    Docente buscarPorUsername(String username);

    /**
     * Asigna un tipo de vinculación a un docente.
     *
     * @param idDocente el identificador del docente
     * @param idTipoVinculacion el identificador del tipo de vinculación
     * @return el docente actualizado
     */
    Docente asignarTipoVinculacion(Integer idDocente, Integer idTipoVinculacion);

    /**
     * Habilita a un docente para dictar una materia.
     *
     * @param idDocente el identificador del docente
     * @param idMateria el identificador de la materia
     * @return el docente actualizado
     */
    Docente habilitarMateria(Integer idDocente, Integer idMateria);

    /**
     * Retira la habilitación de un docente para dictar una materia.
     *
     * @param idDocente el identificador del docente
     * @param idMateria el identificador de la materia
     * @return el docente actualizado
     */
    Docente deshabilitarMateria(Integer idDocente, Integer idMateria);

    /**
     * Agrega una franja bloqueada a un docente.
     *
     * @param idDocente el identificador del docente
     * @param franja la franja horaria
     * @return el docente actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la franja se
     *         solapa con otra restricción
     */
    Docente agregarRestriccion(Integer idDocente, FranjaHoraria franja);

    /**
     * Quita una restricción horaria de un docente.
     *
     * @param idDocente el identificador del docente
     * @param idRestriccion el identificador de la restricción horaria
     * @return el docente actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si el docente
     *         no tiene esa restricción
     */
    Docente quitarRestriccion(Integer idDocente, Integer idRestriccion);

    /**
     * Calcula las horas semanales asignadas a un docente en sus grupos no cerrados del periodo.
     *
     * @param idDocente el identificador del docente
     * @param idPeriodo el identificador del periodo académico
     * @return las horas asignadas
     */
    double calcularHorasAsignadas(Integer idDocente, Integer idPeriodo);

    /**
     * Calcula las horas que un docente aún puede asumir en un periodo.
     *
     * @param idDocente el identificador del docente
     * @param idPeriodo el identificador del periodo académico
     * @return las horas máximas de su vinculación menos las asignadas
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el docente no
     *         tiene tipo de vinculación
     */
    double calcularHorasDisponibles(Integer idDocente, Integer idPeriodo);

    /**
     * Lista los docentes de un tipo de vinculación.
     *
     * @param idTipoVinculacion el identificador del tipo de vinculación
     * @return los docentes con esa vinculación
     */
    List<Docente> listarPorTipoVinculacion(Integer idTipoVinculacion);

    /**
     * Lista los docentes habilitados para una materia.
     *
     * @param idMateria el identificador de la materia
     * @return los docentes habilitados
     */
    List<Docente> listarHabilitadosParaMateria(Integer idMateria);
}
