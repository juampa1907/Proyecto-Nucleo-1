package co.edu.unbosque.collegeAdministrator.dto;

import java.util.List;
import java.util.Map;


/**
 * Resultado de reasignar los estudiantes de un grupo cerrado a otro grupo.
 *
 * @param reasignados los nombres de los estudiantes reasignados
 * @param noReasignados los estudiantes no reasignados y el motivo de cada uno
 */
public record ResultadoReasignacion(List<String> reasignados, Map<String, String> noReasignados) {
}
