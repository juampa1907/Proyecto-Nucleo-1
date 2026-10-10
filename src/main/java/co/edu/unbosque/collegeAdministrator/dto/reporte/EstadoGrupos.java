package co.edu.unbosque.collegeAdministrator.dto.reporte;

import java.util.List;

/**
 * HU-44: grupos abiertos, cerrados y en riesgo de cierre (abiertos con menos inscritos que el mínimo).
 */
public record EstadoGrupos(List<ResumenGrupo> abiertos, List<ResumenGrupo> cerrados, List<ResumenGrupo> enRiesgo) {
}
