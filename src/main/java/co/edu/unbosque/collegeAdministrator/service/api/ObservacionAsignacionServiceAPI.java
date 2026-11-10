package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.ObservacionAsignacion;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

/**
 * Operaciones sobre las observaciones de asignación de los docentes.
 */
public interface ObservacionAsignacionServiceAPI extends GenericServiceAPI<ObservacionAsignacion, Integer> {

    /**
     * Registra la observación del docente asignado a un grupo con horario sin publicar.
     *
     * @param idGrupo el identificador del grupo
     * @param idDocente el identificador del docente
     * @param texto el texto de la observación
     * @return la observación registrada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el texto está
     *         vacío, el docente no es el del grupo o el horario ya se publicó
     */
    ObservacionAsignacion registrar(Integer idGrupo, Integer idDocente, String texto);

    /**
     * Marca una observación como resuelta.
     *
     * @param idObservacion el identificador de la observación
     * @return la observación actualizada
     */
    ObservacionAsignacion resolver(Integer idObservacion);

    /**
     * Lista las observaciones pendientes de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return las observaciones pendientes
     */
    List<ObservacionAsignacion> listarPendientes(Integer idPeriodo);
}
