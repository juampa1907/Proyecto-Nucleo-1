package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Credenciales para iniciar sesión.
 *
 * @param username el nombre de usuario
 * @param password la contraseña del usuario
 */
public record LoginRequest(@NotBlank String username, @NotBlank String password) {
}
