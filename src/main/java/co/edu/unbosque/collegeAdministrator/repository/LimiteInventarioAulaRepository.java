package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.LimiteInventarioAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de persistencia de {@code LimiteInventarioAula}.
 */
public interface LimiteInventarioAulaRepository extends JpaRepository<LimiteInventarioAula, Integer> {

    /**
     * Busca el tope de un tipo de espacio en una sede.
     *
     * @param tipoEspacio el tipo de espacio del aula
     * @param sede la sede de la universidad
     * @return el tope, si existe
     */
    Optional<LimiteInventarioAula> findByTipoEspacioAndSede(TipoEspacio tipoEspacio, Sede sede);
}
