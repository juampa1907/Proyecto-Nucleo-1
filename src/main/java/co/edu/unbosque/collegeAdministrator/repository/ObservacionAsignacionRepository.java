package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.ObservacionAsignacion;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoObservacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de persistencia de {@code ObservacionAsignacion}.
 */
public interface ObservacionAsignacionRepository extends JpaRepository<ObservacionAsignacion, Integer> {

    /**
     * Busca las observaciones de un periodo en un estado.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param estado el estado de la observación
     * @return las observaciones que cumplen ambos criterios
     */
    List<ObservacionAsignacion> findByGrupo_Periodo_IdPeriodoAndEstado(Integer idPeriodo, EstadoObservacion estado);

    /**
     * Indica si un periodo tiene observaciones en un estado.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param estado el estado de la observación
     * @return {@code true} si existe alguna
     */
    boolean existsByGrupo_Periodo_IdPeriodoAndEstado(Integer idPeriodo, EstadoObservacion estado);
}
