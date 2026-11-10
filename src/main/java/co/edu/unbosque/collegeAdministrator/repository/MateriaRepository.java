package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de persistencia de {@code Materia}.
 */
public interface MateriaRepository extends JpaRepository<Materia, Integer> {

    /**
     * Busca materias cuyo nombre contenga un texto, sin distinguir mayúsculas.
     *
     * @param nombre el texto buscado
     * @return las materias que coinciden
     */
    List<Materia> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Indica si existe una materia con ese nombre, sin distinguir mayúsculas.
     *
     * @param nombre el nombre buscado
     * @return {@code true} si existe
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Indica si existe una materia con ese código, sin distinguir mayúsculas.
     *
     * @param codigo el código buscado
     * @return {@code true} si existe
     */
    boolean existsByCodigoIgnoreCase(String codigo);
}
