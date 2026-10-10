package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotNull;

public record InscripcionRequest(@NotNull Integer idGrupo) {
}
