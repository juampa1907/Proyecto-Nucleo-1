package co.edu.unbosque.collegeAdministrator.dto;

import java.util.List;
import java.util.Map;


public record ResultadoReasignacion(List<String> reasignados, Map<String, String> noReasignados) {
}
