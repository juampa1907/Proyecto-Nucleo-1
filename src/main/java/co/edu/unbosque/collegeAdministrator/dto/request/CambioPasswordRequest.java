package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para que un usuario cambie su contraseña.
 *
 * @param passwordActual la contraseña vigente del usuario
 * @param passwordNueva la nueva contraseña
 */
public record CambioPasswordRequest(@NotBlank String passwordActual, @NotBlank @Size(min = 8) String passwordNueva) {
}
