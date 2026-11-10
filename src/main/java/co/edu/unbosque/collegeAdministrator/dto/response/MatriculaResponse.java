package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalDateTime;

/**
 * Datos de la matrícula de un estudiante en un periodo.
 *
 * @param idMatricula el identificador de la matrícula
 * @param idEstudiante el identificador del estudiante
 * @param periodo el código del periodo académico
 * @param fechaConfirmacion la fecha y hora de confirmación
 * @param codigoVerificacion el código del último comprobante generado, o {@code null} si no hay
 */
public record MatriculaResponse(Integer idMatricula, Integer idEstudiante, String periodo,
                                LocalDateTime fechaConfirmacion, String codigoVerificacion) {
}
