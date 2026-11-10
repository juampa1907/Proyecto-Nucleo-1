package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para registrar una observación de asignación.
 *
 * @param texto el texto de la observación
 */
public record ObservacionRequest(@NotBlank @Size(max = 500) String texto) {
}
