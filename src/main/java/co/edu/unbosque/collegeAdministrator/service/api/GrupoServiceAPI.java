package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

/**
 * Operaciones de negocio sobre los grupos y su programación horaria.
 */
public interface GrupoServiceAPI extends GenericServiceAPI<Grupo, Integer> {


    /**
     * Crea un grupo en un periodo con horario editable.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param idMateria el identificador de la materia
     * @param codigo el código del grupo
     * @param cupoMaximo el cupo máximo, o {@code null} para usar el máximo permitido
     * @return el grupo creado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el código falta o
     *         está repetido, el cupo no es válido o el horario no es editable
     */
    Grupo crearGrupo(Integer idPeriodo, Integer idMateria, String codigo, Integer cupoMaximo);

    /**
     * Lista los grupos de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos del periodo
     */
    List<Grupo> listarPorPeriodo(Integer idPeriodo);

    /**
     * Lista los grupos abiertos de una materia en un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param idMateria el identificador de la materia
     * @return los grupos abiertos
     */
    List<Grupo> listarOferta(Integer idPeriodo, Integer idMateria);

    /**
     * Lista los grupos de un docente en un periodo.
     *
     * @param idDocente el identificador del docente
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos del docente
     */
    List<Grupo> listarPorDocente(Integer idDocente, Integer idPeriodo);

    /**
     * Calcula los cupos libres de un grupo, descontando todas sus inscripciones.
     *
     * @param idGrupo el identificador del grupo
     * @return los cupos disponibles
     */
    long calcularCuposDisponibles(Integer idGrupo);

    /**
     * Asigna un docente a un grupo tras validarlo y guardar una versión del horario.
     *
     * @param idGrupo el identificador del grupo
     * @param idDocente el identificador del docente
     * @return el grupo actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el horario no es
     *         editable, el grupo está cerrado o el docente no cumple las reglas
     */
    Grupo asignarDocente(Integer idGrupo, Integer idDocente);


    /**
     * Agrega una sesión a un grupo tras validarla y guardar una versión del horario.
     *
     * @param idGrupo el identificador del grupo
     * @param franja la franja horaria
     * @param idAula el identificador del aula, o {@code null} para no asignar aula
     * @param usoExcepcional {@code true} si el aula se asigna de forma excepcional (requerido para
     *         auditorios)
     * @return el grupo actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la sesión
     *         incumple alguna regla de programación
     */
    Grupo agregarSesion(Integer idGrupo, FranjaHoraria franja, Integer idAula, boolean usoExcepcional);


    /**
     * Reemplaza una sesión por otra posición. Todo ocurre en la misma transacción: si la nueva posición no es
     * válida se revierte el retiro (HU-25).
     *
     * @param idGrupo el identificador del grupo
     * @param idSesion el identificador de la sesión del grupo
     * @param franja la franja horaria
     * @param idAula el identificador del aula, o {@code null} para no asignar aula
     * @param usoExcepcional {@code true} si el aula se asigna de forma excepcional (requerido para
     *         auditorios)
     * @return el grupo actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la nueva posición
     *         incumple alguna regla de programación
     */
    Grupo moverSesion(Integer idGrupo, Integer idSesion, FranjaHoraria franja, Integer idAula, boolean usoExcepcional);

    /**
     * Quita una sesión de un grupo tras guardar una versión del horario.
     *
     * @param idGrupo el identificador del grupo
     * @param idSesion el identificador de la sesión del grupo
     * @return el grupo actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el horario no es
     *         editable
     */
    Grupo quitarSesion(Integer idGrupo, Integer idSesion);


    /**
     * Genera advertencias cuando las sesiones de un grupo quedan en días consecutivos.
     *
     * @param idGrupo el identificador del grupo
     * @param franjaNueva una franja adicional para evaluar, o {@code null}
     * @return los mensajes de advertencia
     */
    List<String> advertenciasDiasConsecutivos(Integer idGrupo, FranjaHoraria franjaNueva);

    /**
     * Sugiere aulas compatibles con el cupo y los requisitos de la materia del grupo.
     *
     * @param idGrupo el identificador del grupo
     * @return las aulas candidatas
     */
    List<Aula> sugerirAulas(Integer idGrupo);

    /**
     * Cierra un grupo y notifica a los estudiantes afectados.
     *
     * @param idGrupo el identificador del grupo
     * @return el grupo cerrado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el periodo está
     *         cerrado
     */
    Grupo cerrarGrupo(Integer idGrupo);

    /**
     * Lista los grupos abiertos con menos inscritos que el mínimo configurado.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos en riesgo
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el mínimo no está
     *         configurado
     */
    List<Grupo> listarGruposEnRiesgo(Integer idPeriodo);

    /**
     * Cierra los grupos en riesgo de un periodo después del cierre de inscripción.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos cerrados
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el periodo está
     *         cerrado, la inscripción no ha finalizado o el mínimo no está configurado
     */
    List<Grupo> cerrarGruposBajoMinimo(Integer idPeriodo);


    /**
     * Evalúa si una sesión se puede programar, sin modificar el grupo.
     *
     * @param idGrupo el identificador del grupo
     * @param franja la franja horaria
     * @param idAula el identificador del aula, o {@code null} para no asignar aula
     * @return {@code null} si es válida o el motivo por el que no lo es
     */
    String evaluarSesion(Integer idGrupo, FranjaHoraria franja, Integer idAula);

    /**
     * Evalúa si un docente se puede asignar a un grupo, sin modificarlo.
     *
     * @param idGrupo el identificador del grupo
     * @param idDocente el identificador del docente
     * @return {@code null} si es válido o el motivo por el que no lo es
     */
    String evaluarDocente(Integer idGrupo, Integer idDocente);


    /**
     * Agrega una sesión de la propuesta automática, sin guardar versión ni registrar en la bitácora.
     *
     * @param idGrupo el identificador del grupo
     * @param franja la franja horaria
     * @param idAula el identificador del aula, o {@code null} para no asignar aula
     * @return el grupo actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la sesión
     *         incumple alguna regla de programación
     */
    Grupo agregarSesionGenerada(Integer idGrupo, FranjaHoraria franja, Integer idAula);


    /**
     * Asigna un docente de la propuesta automática, sin guardar versión ni registrar en la bitácora.
     *
     * @param idGrupo el identificador del grupo
     * @param idDocente el identificador del docente
     * @return el grupo actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el docente no
     *         cumple las reglas
     */
    Grupo asignarDocenteGenerado(Integer idGrupo, Integer idDocente);
}
