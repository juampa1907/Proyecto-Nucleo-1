package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.VersionHorario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de persistencia de {@code VersionHorario}.
 */
public interface VersionHorarioRepository extends JpaRepository<VersionHorario, Integer> {

    /**
     * Busca las versiones de un periodo, de la más reciente a la más antigua.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return las versiones ordenadas por número descendente
     */
    List<VersionHorario> findByPeriodo_IdPeriodoOrderByNumeroDesc(Integer idPeriodo);

    /**
     * Busca la versión más reciente de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return la última versión, si existe
     */
    Optional<VersionHorario> findFirstByPeriodo_IdPeriodoOrderByNumeroDesc(Integer idPeriodo);
}
