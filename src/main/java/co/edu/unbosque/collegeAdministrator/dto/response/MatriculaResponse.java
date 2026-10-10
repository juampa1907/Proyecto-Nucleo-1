package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;

public record MatriculaResponse(Integer idMatricula, Integer idEstudiante, String periodo,
                                LocalDateTime fechaConfirmacion, String codigoVerificacion) {
}
