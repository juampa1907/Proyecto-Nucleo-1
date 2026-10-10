package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestablecerPasswordRequest(@NotBlank String token, @NotBlank @Size(min = 8) String passwordNueva) {
}
