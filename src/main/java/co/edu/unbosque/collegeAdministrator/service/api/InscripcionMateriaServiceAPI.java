package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.ResultadoReasignacion;
import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.MatriculaPeriodo;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.math.BigDecimal;
import java.util.List;

/**
 * Operaciones de inscripción de materias y matrícula de estudiantes.
 */
public interface InscripcionMateriaServiceAPI extends GenericServiceAPI<InscripcionMateria, Integer> {

    /**
     * Inscribe a un estudiante en un grupo tras validar el periodo, el horario publicado, el historial, los
     * requisitos, los créditos, los cupos y los cruces.
     *
     * @param idEstudiante el identificador del estudiante
     * @param idGrupo el identificador del grupo
     * @return la inscripción creada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si incumple alguna
     *         regla de inscripción
     */
    InscripcionMateria inscribir(Integer idEstudiante, Integer idGrupo);

    /**
     * Cancela una inscripción en curso dentro de la ventana de inscripción.
     *
     * @param idInscripcion el identificador de la inscripción
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la materia no
     *         está en curso, la ventana no está abierta o quedaría por debajo del mínimo de créditos con matrícula
     *         confirmada
     */
    void cancelar(Integer idInscripcion);

    /**
     * Registra la nota final y actualiza el estado a aprobada o reprobada.
     *
     * @param idInscripcion el identificador de la inscripción
     * @param nota la nota final, entre 0.0 y 5.0
     * @return la inscripción actualizada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la nota no es
     *         válida, el periodo está cerrado o la materia fue retirada
     */
    InscripcionMateria registrarNota(Integer idInscripcion, BigDecimal nota);

    /**
     * Lista las inscripciones de un estudiante en un estado.
     *
     * @param idEstudiante el identificador del estudiante
     * @param estado el estado de la materia
     * @return las inscripciones que cumplen ambos criterios
     */
    List<InscripcionMateria> listarPorEstudianteYEstado(Integer idEstudiante, EstadoMateria estado);


    /**
     * Mueve los estudiantes en curso de un grupo cerrado a otro abierto de la misma materia y periodo, y
     * notifica a cada uno.
     *
     * @param idGrupoOrigen el identificador del grupo cerrado
     * @param idGrupoDestino el identificador del grupo destino
     * @return los estudiantes reasignados y los no reasignados con su motivo
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si los grupos no
     *         cumplen las condiciones
     */
    ResultadoReasignacion reasignarEstudiantes(Integer idGrupoOrigen, Integer idGrupoDestino);


    /**
     * Confirma la matrícula de un estudiante validando sus créditos mínimos y máximos (HU-38).
     *
     * @param idEstudiante el identificador del estudiante
     * @param idPeriodo el identificador del periodo académico
     * @return la matrícula confirmada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si no tiene
     *         materias, sus créditos están fuera de rango o la ventana no está abierta
     */
    MatriculaPeriodo confirmarMatricula(Integer idEstudiante, Integer idPeriodo);


    /**
     * Lista las inscripciones no retiradas de un estudiante en un periodo.
     *
     * @param idEstudiante el identificador del estudiante
     * @param idPeriodo el identificador del periodo académico
     * @return la carga académica del periodo
     */
    List<InscripcionMateria> listarCargaDelPeriodo(Integer idEstudiante, Integer idPeriodo);
}
