package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarPasswordRequest(@NotBlank @Email String correo) {
}
