package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de persistencia de {@code Docente}.
 */
public interface DocenteRepository extends JpaRepository<Docente, Integer> {

    /**
     * Busca un docente por nombre de usuario.
     *
     * @param username el nombre de usuario
     * @return el docente, si existe
     */
    Optional<Docente> findByUsername(String username);

    /**
     * Busca los docentes de un tipo de vinculación.
     *
     * @param idTipoVinculacion el identificador del tipo de vinculación
     * @return los docentes con esa vinculación
     */
    List<Docente> findByTipoVinculacion_IdTipoVinculacion(Integer idTipoVinculacion);

    /**
     * Busca los docentes habilitados para una materia.
     *
     * @param idMateria el identificador de la materia
     * @return los docentes habilitados
     */
    List<Docente> findByMateriasHabilitadas_IdMateria(Integer idMateria);
}
