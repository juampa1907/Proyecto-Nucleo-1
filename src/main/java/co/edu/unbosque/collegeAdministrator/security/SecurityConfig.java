package co.edu.unbosque.collegeAdministrator.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


/**
 * Configuración de Spring Security: CORS, sesión sin estado, autenticación JWT y seguridad por método.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /** El servicio que genera y lee los tokens JWT. */
    private final JwtService jwtService;
    /** El servicio que carga los datos de seguridad del usuario. */
    private final UsuarioDetailsService usuarioDetailsService;
    /** El origen autorizado por CORS. */
    private final String urlFrontend;

    /**
     * Crea una instancia de {@code SecurityConfig} con las dependencias requeridas.
     *
     * @param jwtService el servicio que genera y lee los tokens JWT
     * @param usuarioDetailsService el servicio que carga los datos de seguridad del usuario
     * @param urlFrontend el origen autorizado por CORS
     */
    public SecurityConfig(JwtService jwtService, UsuarioDetailsService usuarioDetailsService,
                          @Value("${app.url-frontend}") String urlFrontend) {
        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
        this.urlFrontend = urlFrontend;
    }


    /**
     * Autoriza por CORS al frontend configurado.
     *
     * @return la configuración CORS de todas las rutas
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(List.of(urlFrontend));
        configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuracion.setExposedHeaders(List.of("Content-Disposition"));
        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", configuracion);
        return fuente;
    }


    /**
     * Define el codificador de contraseñas BCrypt.
     *
     * @return el codificador de contraseñas
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Expone el administrador de autenticación de Spring.
     *
     * @param configuracion la configuración de autenticación
     * @return el administrador de autenticación
     * @throws Exception si no se puede obtener
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuracion) throws Exception {
        return configuracion.getAuthenticationManager();
    }

    /**
     * Define la cadena de filtros: rutas públicas, sesión sin estado, filtro JWT y respuesta 401 para
     * solicitudes sin autenticar.
     *
     * @param http el constructor de seguridad HTTP
     * @return la cadena de filtros
     * @throws Exception si la configuración falla
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(autorizacion -> autorizacion
                        // "/error" público para que los errores se respondan con su código real y no como 401.
                        .requestMatchers("/auth/**", "/error").permitAll()
                        .anyRequest().authenticated())
                // Sin token válido → 401. Con token pero sin el rol requerido → 403 (comportamiento por defecto).
                .exceptionHandling(excepciones -> excepciones
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, usuarioDetailsService),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
