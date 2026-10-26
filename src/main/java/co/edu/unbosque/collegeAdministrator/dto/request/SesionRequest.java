package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public record SesionRequest(@NotNull @Valid FranjaRequest franja, Integer idAula, boolean usoExcepcional) {
}
