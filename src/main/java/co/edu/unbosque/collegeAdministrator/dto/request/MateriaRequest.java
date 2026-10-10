package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MateriaRequest(
        @NotBlank @Size(max = 20) String codigo,
        @NotBlank @Size(max = 100) String nombre,
        @NotNull @Positive Integer numeroCreditos,
        @NotNull @Positive Integer horasSemanales) {
}
