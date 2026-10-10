package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Integer> {

    boolean existsByCodigo(String codigo);
}
