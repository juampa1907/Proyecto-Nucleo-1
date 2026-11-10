package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de persistencia de {@code PeriodoAcademico}.
 */
public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Integer> {

    /**
     * Indica si existe un periodo con ese código.
     *
     * @param codigo el código buscado
     * @return {@code true} si existe
     */
    boolean existsByCodigo(String codigo);
}
