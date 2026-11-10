package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Datos para solicitar la recuperación de la contraseña.
 *
 * @param correo el correo electrónico del usuario
 */
public record RecuperarPasswordRequest(@NotBlank @Email String correo) {
}
