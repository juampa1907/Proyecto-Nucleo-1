package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TipoVinculacionRequest(
        @NotBlank @Size(max = 50) String nombre,
        @NotNull @Positive Integer horasMaximasSemanales) {
}
