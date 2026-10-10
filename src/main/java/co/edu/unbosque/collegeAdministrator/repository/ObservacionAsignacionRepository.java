package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.ObservacionAsignacion;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoObservacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacionAsignacionRepository extends JpaRepository<ObservacionAsignacion, Integer> {

    List<ObservacionAsignacion> findByGrupo_Periodo_IdPeriodoAndEstado(Integer idPeriodo, EstadoObservacion estado);

    boolean existsByGrupo_Periodo_IdPeriodoAndEstado(Integer idPeriodo, EstadoObservacion estado);
}
