package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocenteRepository extends JpaRepository<Docente, Integer> {

    Optional<Docente> findByUsername(String username);

    List<Docente> findByTipoVinculacion_IdTipoVinculacion(Integer idTipoVinculacion);

    List<Docente> findByMateriasHabilitadas_IdMateria(Integer idMateria);
}
