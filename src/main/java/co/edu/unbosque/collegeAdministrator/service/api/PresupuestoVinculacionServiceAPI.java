package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.PresupuestoVinculacion;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;


/**
 * Operaciones sobre el presupuesto de horas por tipo de vinculación (HU-43).
 */
public interface PresupuestoVinculacionServiceAPI extends GenericServiceAPI<PresupuestoVinculacion, Integer> {

    /**
     * Crea o actualiza el presupuesto de un tipo de vinculación en un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param idTipoVinculacion el identificador del tipo de vinculación
     * @param horasPresupuestadas las horas presupuestadas
     * @return el presupuesto guardado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si las horas son
     *         negativas o el periodo está cerrado
     */
    PresupuestoVinculacion registrar(Integer idPeriodo, Integer idTipoVinculacion, Integer horasPresupuestadas);

    /**
     * Lista los presupuestos de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return los presupuestos del periodo
     */
    List<PresupuestoVinculacion> listarPorPeriodo(Integer idPeriodo);
}
