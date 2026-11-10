package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.MatriculaPeriodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de persistencia de {@code MatriculaPeriodo}.
 */
public interface MatriculaPeriodoRepository extends JpaRepository<MatriculaPeriodo, Integer> {

    /**
     * Busca la matrícula de un estudiante en un periodo.
     *
     * @param idEstudiante el identificador del estudiante
     * @param idPeriodo el identificador del periodo académico
     * @return la matrícula, si existe
     */
    Optional<MatriculaPeriodo> findByEstudiante_IdUsuarioAndPeriodo_IdPeriodo(Integer idEstudiante, Integer idPeriodo);
}
