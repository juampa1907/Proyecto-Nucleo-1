package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.PresupuestoVinculacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de persistencia de {@code PresupuestoVinculacion}.
 */
public interface PresupuestoVinculacionRepository extends JpaRepository<PresupuestoVinculacion, Integer> {

    /**
     * Busca los presupuestos de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return los presupuestos del periodo
     */
    List<PresupuestoVinculacion> findByPeriodo_IdPeriodo(Integer idPeriodo);

    /**
     * Busca el presupuesto de un tipo de vinculación en un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param idTipoVinculacion el identificador del tipo de vinculación
     * @return el presupuesto, si existe
     */
    Optional<PresupuestoVinculacion> findByPeriodo_IdPeriodoAndTipoVinculacion_IdTipoVinculacion(Integer idPeriodo,
                                                                                                 Integer idTipoVinculacion);
}
