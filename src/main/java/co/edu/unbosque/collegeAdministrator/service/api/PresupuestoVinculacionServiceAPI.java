package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.PresupuestoVinculacion;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

/**
 * HU-43: presupuesto de horas por tipo de vinculación en cada periodo.
 */
public interface PresupuestoVinculacionServiceAPI extends GenericServiceAPI<PresupuestoVinculacion, Integer> {

    PresupuestoVinculacion registrar(Integer idPeriodo, Integer idTipoVinculacion, Integer horasPresupuestadas);

    List<PresupuestoVinculacion> listarPorPeriodo(Integer idPeriodo);
}
