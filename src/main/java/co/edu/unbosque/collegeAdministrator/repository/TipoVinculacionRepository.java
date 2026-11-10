package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.TipoVinculacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de persistencia de {@code TipoVinculacion}.
 */
public interface TipoVinculacionRepository extends JpaRepository<TipoVinculacion, Integer> {

    /**
     * Busca un tipo de vinculación por nombre.
     *
     * @param nombre el nombre buscado
     * @return el tipo de vinculación, si existe
     */
    Optional<TipoVinculacion> findByNombre(String nombre);
}
