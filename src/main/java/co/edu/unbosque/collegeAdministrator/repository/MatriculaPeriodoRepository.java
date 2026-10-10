package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.MatriculaPeriodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatriculaPeriodoRepository extends JpaRepository<MatriculaPeriodo, Integer> {

    Optional<MatriculaPeriodo> findByEstudiante_IdUsuarioAndPeriodo_IdPeriodo(Integer idEstudiante, Integer idPeriodo);
}
