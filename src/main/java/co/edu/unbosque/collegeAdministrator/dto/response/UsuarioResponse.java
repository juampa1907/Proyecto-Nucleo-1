package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;

import java.time.LocalDateTime;
import java.util.Set;


public record UsuarioResponse(Integer idUsuario, String username, String correo, String tipo, Set<Rol> roles,
                              LocalDateTime ultimoInicioSesion, String ipUltimoInicioSesion, LocalDateTime bloqueadoHasta) {
}
