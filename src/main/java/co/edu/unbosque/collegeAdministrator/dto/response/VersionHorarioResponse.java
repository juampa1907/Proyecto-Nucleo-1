package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;

public record VersionHorarioResponse(Integer idVersion, Integer numero, LocalDateTime fecha, String usuario,
                                     String descripcion) {
}
