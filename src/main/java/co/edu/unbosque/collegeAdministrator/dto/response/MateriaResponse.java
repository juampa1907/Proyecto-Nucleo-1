package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;

import java.util.List;
import java.util.Set;

/**
 * Datos de una materia.
 *
 * @param idMateria el identificador de la materia
 * @param codigo el código de la materia
 * @param nombre el nombre de la materia
 * @param numeroCreditos el número de créditos de la materia
 * @param horasSemanales las horas semanales de la materia
 * @param requisitosAula las características de aula exigidas por la materia
 * @param prerrequisitos los prerrequisitos ordenados por código
 * @param correquisitos los correquisitos ordenados por código
 */
public record MateriaResponse(Integer idMateria, String codigo, String nombre, Integer numeroCreditos,
                              Integer horasSemanales, Set<CaracteristicaAula> requisitosAula,
                              List<MateriaResumen> prerrequisitos, List<MateriaResumen> correquisitos) {
}
