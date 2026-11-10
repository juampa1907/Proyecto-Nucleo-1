package co.edu.unbosque.collegeAdministrator.dto.request;

import co.edu.unbosque.collegeAdministrator.entity.enums.Escalafon;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para actualizar el perfil de un docente.
 *
 * @param correo el correo electrónico del usuario
 * @param escalafon el escalafón del docente
 * @param especialidad la especialidad del docente
 */
public record DocentePerfilRequest(
        @NotBlank @Email @Size(max = 255) String correo,
        Escalafon escalafon,
        @Size(max = 100) String especialidad) {
}
