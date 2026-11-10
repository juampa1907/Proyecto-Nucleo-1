package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear un estudiante.
 *
 * @param username el nombre de usuario
 * @param correo el correo electrónico del usuario
 * @param password la contraseña del usuario
 * @param numeroSemestre el semestre que cursa el estudiante
 */
public record EstudianteRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email @Size(max = 255) String correo,
        @NotBlank @Size(min = 8) String password,
        @NotNull @Positive Integer numeroSemestre) {
}
