package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


/**
 * Datos para crear un grupo.
 *
 * @param idPeriodo el identificador del periodo académico
 * @param idMateria el identificador de la materia
 * @param codigo el código del grupo
 * @param cupoMaximo el cupo máximo, o {@code null} para usar el máximo permitido
 */
public record GrupoRequest(
        @NotNull Integer idPeriodo,
        @NotNull Integer idMateria,
        @NotBlank @Size(max = 10) String codigo,
        @Positive Integer cupoMaximo) {
}
