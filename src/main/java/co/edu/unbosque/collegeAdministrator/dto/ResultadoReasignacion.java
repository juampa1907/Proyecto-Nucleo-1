package co.edu.unbosque.collegeAdministrator.dto;

import java.util.List;
import java.util.Map;

/**
 * HU-36: estudiantes reasignados y, para los que no se pudo, el motivo (username → motivo).
 */
public record ResultadoReasignacion(List<String> reasignados, Map<String, String> noReasignados) {
}
