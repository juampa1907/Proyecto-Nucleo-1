package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de persistencia de {@code Usuario}.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por nombre de usuario.
     *
     * @param username el nombre de usuario
     * @return el usuario, si existe
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por correo electrónico.
     *
     * @param correo el correo electrónico del usuario
     * @return el usuario, si existe
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Indica si un nombre de usuario ya está registrado.
     *
     * @param username el nombre de usuario
     * @return {@code true} si existe
     */
    boolean existsByUsername(String username);

    /**
     * Indica si un correo electrónico ya está registrado.
     *
     * @param correo el correo electrónico del usuario
     * @return {@code true} si existe
     */
    boolean existsByCorreo(String correo);
}
