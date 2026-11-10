package co.edu.unbosque.collegeAdministrator.dto.request;

import co.edu.unbosque.collegeAdministrator.entity.enums.Escalafon;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * Datos para crear un docente.
 *
 * @param username el nombre de usuario
 * @param correo el correo electrónico del usuario
 * @param password la contraseña del usuario
 * @param escalafon el escalafón del docente
 * @param especialidad la especialidad del docente
 * @param idTipoVinculacion el identificador del tipo de vinculación, u {@code null} para asignarlo después
 */
public record DocenteRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email @Size(max = 255) String correo,
        @NotBlank @Size(min = 8) String password,
        Escalafon escalafon,
        @Size(max = 100) String especialidad,
        Integer idTipoVinculacion) {
}
