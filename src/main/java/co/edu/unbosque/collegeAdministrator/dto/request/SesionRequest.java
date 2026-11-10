package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


/**
 * Datos para agregar o mover una sesión de un grupo.
 *
 * @param franja la franja horaria de la sesión
 * @param idAula el identificador del aula, o {@code null} para dejar la sesión sin aula
 * @param usoExcepcional {@code true} si el aula se asigna de forma excepcional (requerido para auditorios)
 */
public record SesionRequest(@NotNull @Valid FranjaRequest franja, Integer idAula, boolean usoExcepcional) {
}
