package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.TipoVinculacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoVinculacionRepository extends JpaRepository<TipoVinculacion, Integer> {

    Optional<TipoVinculacion> findByNombre(String nombre);
}
