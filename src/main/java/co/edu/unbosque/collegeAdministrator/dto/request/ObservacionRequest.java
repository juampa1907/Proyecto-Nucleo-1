package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ObservacionRequest(@NotBlank @Size(max = 500) String texto) {
}
