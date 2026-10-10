package co.edu.unbosque.collegeAdministrator.dto.request;

import co.edu.unbosque.collegeAdministrator.entity.enums.Escalafon;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @param idTipoVinculacion opcional al crear; se puede asignar después.
 */
public record DocenteRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email @Size(max = 255) String correo,
        @NotBlank @Size(min = 8) String password,
        Escalafon escalafon,
        @Size(max = 100) String especialidad,
        Integer idTipoVinculacion) {
}
