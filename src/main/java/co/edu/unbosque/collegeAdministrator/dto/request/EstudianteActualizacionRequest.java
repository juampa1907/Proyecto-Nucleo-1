package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record EstudianteActualizacionRequest(
        @NotBlank @Email @Size(max = 255) String correo,
        @NotNull @Positive Integer numeroSemestre) {
}
