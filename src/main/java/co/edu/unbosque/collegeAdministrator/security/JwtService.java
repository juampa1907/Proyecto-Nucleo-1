package co.edu.unbosque.collegeAdministrator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * Genera y lee los tokens JWT firmados de la aplicación.
 */
@Service
public class JwtService {

    /** Longitud mínima en bytes del secreto de firma. */
    private static final int LONGITUD_MINIMA_CLAVE = 32;

    /** La clave con la que se firman y verifican los tokens. */
    private final SecretKey clave;
    /** La vigencia de los tokens en minutos. */
    private final long minutosExpiracion;

    /**
     * Crea el servicio a partir de la configuración.
     *
     * @param secreto el secreto de firma
     * @param minutosExpiracion la vigencia de los tokens en minutos
     * @throws IllegalStateException si el secreto es demasiado corto
     */
    public JwtService(@Value("${app.jwt.secret}") String secreto,
                      @Value("${app.jwt.expiracion-minutos:60}") long minutosExpiracion) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < LONGITUD_MINIMA_CLAVE) {
            throw new IllegalStateException("app.jwt.secret debe tener al menos " + LONGITUD_MINIMA_CLAVE + " caracteres");
        }
        this.clave = Keys.hmacShaKeyFor(bytes);
        this.minutosExpiracion = minutosExpiracion;
    }

    /**
     * Genera un token firmado con el usuario y sus roles.
     *
     * @param usuario el usuario autenticado
     * @return el token JWT compacto
     */
    public String generarToken(UserDetails usuario) {
        Instant ahora = Instant.now();
        List<String> roles = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("roles", roles)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(minutosExpiracion, ChronoUnit.MINUTES)))
                .signWith(clave)
                .compact();
    }

    /**
     * Obtiene el nombre de usuario de un token.
     *
     * @param token el token JWT
     * @return el nombre de usuario contenido en el token
     */
    public String extraerUsername(String token) {
        return leerClaims(token).getSubject();
    }

    /**
     * Verifica un token y lee su contenido.
     *
     * @param token el token JWT
     * @return los datos contenidos en el token
     */
    private Claims leerClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
