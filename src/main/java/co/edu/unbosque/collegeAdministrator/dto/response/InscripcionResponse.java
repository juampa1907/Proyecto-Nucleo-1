package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;

import java.math.BigDecimal;

public record InscripcionResponse(Integer idInscripcion, Integer idEstudiante, String estudiante, Integer idGrupo,
                                  String grupo, MateriaResumen materia, String periodo, EstadoMateria estado,
                                  BigDecimal notaFinal) {
}
