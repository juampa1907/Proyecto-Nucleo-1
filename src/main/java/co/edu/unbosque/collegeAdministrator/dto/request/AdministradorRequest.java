package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear un usuario administrador.
 *
 * @param username el nombre de usuario
 * @param correo el correo electrónico del usuario
 * @param password la contraseña del usuario
 */
public record AdministradorRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email @Size(max = 255) String correo,
        @NotBlank @Size(min = 8) String password) {
}
