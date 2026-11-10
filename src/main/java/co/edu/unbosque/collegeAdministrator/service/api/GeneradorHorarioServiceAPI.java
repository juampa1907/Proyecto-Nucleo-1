package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.ResultadoGeneracion;


/**
 * Generación automática de propuestas de horario.
 */
public interface GeneradorHorarioServiceAPI {

    /**
     * Programa las sesiones faltantes de los grupos abiertos de un periodo y asigna docentes a los que no
     * tienen. Guarda antes una versión del horario.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos programados y los que no se pudieron completar
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el periodo está
     *         cerrado o su horario ya fue publicado
     */
    ResultadoGeneracion generarPropuesta(Integer idPeriodo);
}
