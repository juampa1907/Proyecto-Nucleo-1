package co.edu.unbosque.collegeAdministrator.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * Filtro que autentica cada solicitud a partir del token JWT del encabezado {@code Authorization}.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Prefijo del token en el encabezado {@code Authorization}. */
    private static final String PREFIJO = "Bearer ";

    /** El servicio que genera y lee los tokens JWT. */
    private final JwtService jwtService;
    /** El servicio que carga los datos de seguridad del usuario. */
    private final UsuarioDetailsService usuarioDetailsService;

    /**
     * Crea una instancia de {@code JwtAuthenticationFilter} con las dependencias requeridas.
     *
     * @param jwtService el servicio que genera y lee los tokens JWT
     * @param usuarioDetailsService el servicio que carga los datos de seguridad del usuario
     */
    public JwtAuthenticationFilter(JwtService jwtService, UsuarioDetailsService usuarioDetailsService) {
        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    /**
     * Autentica la solicitud si trae un token válido y el usuario está habilitado y sin bloqueo; un token
     * inválido deja la solicitud sin autenticar.
     *
     * @param request la solicitud HTTP
     * @param response la respuesta HTTP
     * @param chain la cadena de filtros
     * @throws ServletException si falla un filtro posterior
     * @throws IOException si ocurre un error de entrada o salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String encabezado = request.getHeader("Authorization");
        if (encabezado != null && encabezado.startsWith(PREFIJO)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String username = jwtService.extraerUsername(encabezado.substring(PREFIJO.length()));
                UserDetails usuario = usuarioDetailsService.loadUserByUsername(username);
                if (usuario.isEnabled() && usuario.isAccountNonLocked()) {
                    UsernamePasswordAuthenticationToken autenticacion =
                            new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(autenticacion);
                }
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
