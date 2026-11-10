package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de persistencia de {@code ParametrosAcademicos}.
 */
public interface ParametrosAcademicosRepository extends JpaRepository<ParametrosAcademicos, Integer> {

    /**
     * Busca la primera fila de parámetros.
     *
     * @return los parámetros, si ya existen
     */
    Optional<ParametrosAcademicos> findFirstByOrderByIdParametrosAsc();
}
