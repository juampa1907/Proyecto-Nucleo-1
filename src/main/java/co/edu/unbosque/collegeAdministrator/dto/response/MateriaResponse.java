package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;

import java.util.List;
import java.util.Set;

public record MateriaResponse(Integer idMateria, String codigo, String nombre, Integer numeroCreditos,
                              Integer horasSemanales, Set<CaracteristicaAula> requisitosAula,
                              List<MateriaResumen> prerrequisitos, List<MateriaResumen> correquisitos) {
}
