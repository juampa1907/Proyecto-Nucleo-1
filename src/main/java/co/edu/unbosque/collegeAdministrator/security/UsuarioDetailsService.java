package co.edu.unbosque.collegeAdministrator.security;

import co.edu.unbosque.collegeAdministrator.entity.Usuario;
import co.edu.unbosque.collegeAdministrator.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Carga los datos de seguridad de un usuario desde la base de datos.
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    /** El repositorio de persistencia de usuarios. */
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea una instancia de {@code UsuarioDetailsService} con las dependencias requeridas.
     *
     * @param usuarioRepository el repositorio de persistencia de usuarios
     */
    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga un usuario para Spring Security. Un usuario sin roles queda deshabilitado (HU-02) y uno con
     * bloqueo vigente queda bloqueado (HU-01).
     *
     * @param username el nombre de usuario
     * @return los datos de seguridad del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario " + username));
        List<SimpleGrantedAuthority> autoridades = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.name()))
                .toList();
        boolean bloqueado = usuario.getBloqueadoHasta() != null
                && usuario.getBloqueadoHasta().isAfter(LocalDateTime.now());
        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(autoridades)
                .disabled(autoridades.isEmpty()) // HU-02: un usuario sin rol asignado no puede ingresar.
                .accountLocked(bloqueado)        // HU-01: bloqueo temporal tras 3 intentos fallidos.
                .build();
    }
}
