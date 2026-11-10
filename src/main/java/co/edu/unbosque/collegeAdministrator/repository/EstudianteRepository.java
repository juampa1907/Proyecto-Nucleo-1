package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de persistencia de {@code Estudiante}.
 */
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    /**
     * Busca un estudiante por nombre de usuario.
     *
     * @param username el nombre de usuario
     * @return el estudiante, si existe
     */
    Optional<Estudiante> findByUsername(String username);
}
