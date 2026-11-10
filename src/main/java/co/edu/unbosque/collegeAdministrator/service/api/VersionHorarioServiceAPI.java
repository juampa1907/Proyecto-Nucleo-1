package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.VersionHorario;

import java.util.List;


/**
 * Operaciones sobre las versiones guardadas del horario (HU-29).
 */
public interface VersionHorarioServiceAPI {

    /** Número máximo de versiones que se conservan por periodo. */
    int MAX_VERSIONES = 5;

    /**
     * Guarda una copia del horario actual y elimina las versiones más antiguas que excedan el máximo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param descripcion la descripción de la versión
     * @return la versión guardada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si el periodo
     *         no existe
     */
    VersionHorario guardarVersion(Integer idPeriodo, String descripcion);

    /**
     * Lista las versiones de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return las versiones, de la más reciente a la más antigua
     */
    List<VersionHorario> listarVersiones(Integer idPeriodo);

    /**
     * Restaura una versión del horario tras guardar una copia del estado actual.
     *
     * @param idVersion el identificador de la versión del horario
     * @return la copia de respaldo del estado anterior
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si la versión
     *         no existe
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el horario no
     *         está en borrador
     */
    VersionHorario restaurar(Integer idVersion);
}
