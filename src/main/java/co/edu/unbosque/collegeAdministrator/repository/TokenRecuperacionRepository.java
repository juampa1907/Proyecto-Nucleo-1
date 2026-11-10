package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.TokenRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de persistencia de {@code TokenRecuperacion}.
 */
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Integer> {

    /**
     * Busca un registro por el valor del token.
     *
     * @param token el valor del token
     * @return el registro, si existe
     */
    Optional<TokenRecuperacion> findByToken(String token);
}
