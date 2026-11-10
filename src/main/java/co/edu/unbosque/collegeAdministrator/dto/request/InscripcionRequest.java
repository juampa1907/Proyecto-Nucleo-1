package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Datos para inscribir una materia.
 *
 * @param idGrupo el identificador del grupo
 */
public record InscripcionRequest(@NotNull Integer idGrupo) {
}
