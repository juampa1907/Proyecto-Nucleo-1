package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParametrosAcademicosRepository extends JpaRepository<ParametrosAcademicos, Integer> {

    Optional<ParametrosAcademicos> findFirstByOrderByIdParametrosAsc();
}
