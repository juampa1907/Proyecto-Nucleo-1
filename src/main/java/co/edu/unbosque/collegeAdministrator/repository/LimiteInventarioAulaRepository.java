package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.LimiteInventarioAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LimiteInventarioAulaRepository extends JpaRepository<LimiteInventarioAula, Integer> {

    Optional<LimiteInventarioAula> findByTipoEspacioAndSede(TipoEspacio tipoEspacio, Sede sede);
}
