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


@Component
public class UsuarioActual {

    public static final String USUARIO_SISTEMA = "sistema";

    private final UsuarioRepository usuarioRepository;

    public UsuarioActual(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public String obtenerUsername() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken) {
            return USUARIO_SISTEMA;
        }
        return autenticacion.getName();
    }

    public Integer obtenerIdUsuario() {
        return usuarioRepository.findByUsername(obtenerUsername())
                .map(Usuario::getIdUsuario)
                .orElseThrow(() -> new AutenticacionException("No hay un usuario autenticado"));
    }

    public boolean tieneAlgunRol(Rol... roles) {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null) {
            return false;
        }
        return autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> Arrays.stream(roles)
                        .anyMatch(rol -> ("ROLE_" + rol.name()).equals(autoridad.getAuthority())));
    }

    public boolean esAdministrador() {
        return tieneAlgunRol(Rol.ADMINISTRADOR);
    }

    public void verificarPropietarioOAdmin(Integer idUsuario) {
        verificarPropietarioOAlgunRol(idUsuario, Rol.ADMINISTRADOR);
    }


    public void verificarPropietarioOAlgunRol(Integer idUsuario, Rol... rolesAutorizados) {
        if (tieneAlgunRol(rolesAutorizados)) {
            return;
        }
        if (!obtenerIdUsuario().equals(idUsuario)) {
            throw new AccesoDenegadoException("No tienes permiso para consultar la información de otro usuario");
        }
    }
}
