package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para restablecer la contraseña con un enlace de recuperación.
 *
 * @param token el token recibido en el enlace
 * @param passwordNueva la nueva contraseña
 */
public record RestablecerPasswordRequest(@NotBlank String token, @NotBlank @Size(min = 8) String passwordNueva) {
}
