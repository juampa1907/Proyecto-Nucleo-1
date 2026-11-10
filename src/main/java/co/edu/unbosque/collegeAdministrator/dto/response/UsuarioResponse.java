package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;

import java.time.LocalDateTime;
import java.util.Set;


/**
 * Datos de un usuario.
 *
 * @param idUsuario el identificador del usuario
 * @param username el nombre de usuario
 * @param correo el correo electrónico del usuario
 * @param tipo el tipo de usuario: {@code ESTUDIANTE}, {@code DOCENTE} o {@code USUARIO}
 * @param roles los roles del usuario
 * @param ultimoInicioSesion la fecha y hora del último inicio de sesión
 * @param ipUltimoInicioSesion la dirección IP del último inicio de sesión
 * @param bloqueadoHasta la fecha y hora hasta la que está bloqueado, o {@code null} si no tiene bloqueo
 */
public record UsuarioResponse(Integer idUsuario, String username, String correo, String tipo, Set<Rol> roles,
                              LocalDateTime ultimoInicioSesion, String ipUltimoInicioSesion, LocalDateTime bloqueadoHasta) {
}
