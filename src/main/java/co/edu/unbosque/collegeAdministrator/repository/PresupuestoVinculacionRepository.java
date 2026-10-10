package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.PresupuestoVinculacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresupuestoVinculacionRepository extends JpaRepository<PresupuestoVinculacion, Integer> {

    List<PresupuestoVinculacion> findByPeriodo_IdPeriodo(Integer idPeriodo);

    Optional<PresupuestoVinculacion> findByPeriodo_IdPeriodoAndTipoVinculacion_IdTipoVinculacion(Integer idPeriodo,
                                                                                                 Integer idTipoVinculacion);
}
