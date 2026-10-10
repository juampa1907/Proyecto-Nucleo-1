package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.VersionHorario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VersionHorarioRepository extends JpaRepository<VersionHorario, Integer> {

    List<VersionHorario> findByPeriodo_IdPeriodoOrderByNumeroDesc(Integer idPeriodo);

    Optional<VersionHorario> findFirstByPeriodo_IdPeriodoOrderByNumeroDesc(Integer idPeriodo);
}
