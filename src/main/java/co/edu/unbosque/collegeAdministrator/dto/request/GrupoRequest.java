package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record GrupoRequest(
        @NotNull Integer idPeriodo,
        @NotNull Integer idMateria,
        @NotBlank @Size(max = 10) String codigo,
        @Positive Integer cupoMaximo) {
}
