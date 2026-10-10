package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * HU-04: incluye fecha, hora e IP del último inicio de sesión. Nunca incluye la contraseña.
 *
 * @param tipo ESTUDIANTE, DOCENTE o USUARIO (administrador u otro usuario sin perfil académico).
 */
public record UsuarioResponse(Integer idUsuario, String username, String correo, String tipo, Set<Rol> roles,
                              LocalDateTime ultimoInicioSesion, String ipUltimoInicioSesion, LocalDateTime bloqueadoHasta) {
}
