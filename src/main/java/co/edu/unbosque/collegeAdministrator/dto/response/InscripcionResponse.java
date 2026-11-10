package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;

import java.math.BigDecimal;

/**
 * Datos de una inscripción.
 *
 * @param idInscripcion el identificador de la inscripción
 * @param idEstudiante el identificador del estudiante
 * @param estudiante el nombre del estudiante
 * @param idGrupo el identificador del grupo
 * @param grupo el código del grupo
 * @param materia el resumen de la materia
 * @param periodo el código del periodo académico
 * @param estado el estado de la materia inscrita
 * @param notaFinal la nota final, o {@code null} si no se ha registrado
 */
public record InscripcionResponse(Integer idInscripcion, Integer idEstudiante, String estudiante, Integer idGrupo,
                                  String grupo, MateriaResumen materia, String periodo, EstadoMateria estado,
                                  BigDecimal notaFinal) {
}
