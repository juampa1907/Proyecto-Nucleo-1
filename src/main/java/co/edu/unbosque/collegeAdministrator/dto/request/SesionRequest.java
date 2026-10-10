package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @param idAula         opcional: el aula se puede asignar después.
 * @param usoExcepcional obligatorio en true para usar un auditorio (ESP-F-05).
 */
public record SesionRequest(@NotNull @Valid FranjaRequest franja, Integer idAula, boolean usoExcepcional) {
}
