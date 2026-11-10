package co.edu.unbosque.collegeAdministrator.security;

import co.edu.unbosque.collegeAdministrator.entity.Usuario;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import co.edu.unbosque.collegeAdministrator.repository.UsuarioRepository;
import co.edu.unbosque.collegeAdministrator.utils.exception.AccesoDenegadoException;
import co.edu.unbosque.collegeAdministrator.utils.exception.AutenticacionException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * Da acceso al usuario autenticado y verifica sus permisos sobre la información.
 */
@Component
public class UsuarioActual {

    /** Nombre usado cuando la operación no proviene de un usuario autenticado. */
    public static final String USUARIO_SISTEMA = "sistema";

    /** El repositorio de persistencia de usuarios. */
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea una instancia de {@code UsuarioActual} con las dependencias requeridas.
     *
     * @param usuarioRepository el repositorio de persistencia de usuarios
     */
    public UsuarioActual(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    /**
     * Obtiene el nombre del usuario autenticado.
     *
     * @return el nombre de usuario, o {@link #USUARIO_SISTEMA} si no hay autenticación
     */
    public String obtenerUsername() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken) {
            return USUARIO_SISTEMA;
        }
        return autenticacion.getName();
    }

    /**
     * Obtiene el identificador del usuario autenticado.
     *
     * @return el identificador del usuario
     * @throws AutenticacionException si no hay un usuario autenticado
     */
    public Integer obtenerIdUsuario() {
        return usuarioRepository.findByUsername(obtenerUsername())
                .map(Usuario::getIdUsuario)
                .orElseThrow(() -> new AutenticacionException("No hay un usuario autenticado"));
    }

    /**
     * Indica si el usuario autenticado tiene alguno de los roles.
     *
     * @param roles los roles aceptados
     * @return {@code true} si tiene al menos uno
     */
    public boolean tieneAlgunRol(Rol... roles) {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null) {
            return false;
        }
        return autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> Arrays.stream(roles)
                        .anyMatch(rol -> ("ROLE_" + rol.name()).equals(autoridad.getAuthority())));
    }

    /**
     * Indica si el usuario autenticado es administrador.
     *
     * @return {@code true} si tiene el rol de administrador
     */
    public boolean esAdministrador() {
        return tieneAlgunRol(Rol.ADMINISTRADOR);
    }

    /**
     * Verifica que el usuario autenticado sea el dueño de la información o un administrador.
     *
     * @param idUsuario el identificador del dueño de la información
     * @throws AccesoDenegadoException si no tiene permiso
     */
    public void verificarPropietarioOAdmin(Integer idUsuario) {
        verificarPropietarioOAlgunRol(idUsuario, Rol.ADMINISTRADOR);
    }


    /**
     * Verifica que el usuario autenticado sea el dueño de la información o tenga un rol autorizado.
     *
     * @param idUsuario el identificador del dueño de la información
     * @param rolesAutorizados los roles con acceso a información ajena
     * @throws AccesoDenegadoException si no tiene permiso
     */
    public void verificarPropietarioOAlgunRol(Integer idUsuario, Rol... rolesAutorizados) {
        if (tieneAlgunRol(rolesAutorizados)) {
            return;
        }
        if (!obtenerIdUsuario().equals(idUsuario)) {
            throw new AccesoDenegadoException("No tienes permiso para consultar la información de otro usuario");
        }
    }
}
