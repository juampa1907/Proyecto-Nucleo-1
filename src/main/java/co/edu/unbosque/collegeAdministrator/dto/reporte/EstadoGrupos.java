package co.edu.unbosque.collegeAdministrator.dto.reporte;

import java.util.List;


/**
 * Clasificación de los grupos de un periodo según su estado.
 *
 * @param abiertos los grupos abiertos
 * @param cerrados los grupos cerrados
 * @param enRiesgo los grupos abiertos con menos inscritos que el mínimo configurado
 */
public record EstadoGrupos(List<ResumenGrupo> abiertos, List<ResumenGrupo> cerrados, List<ResumenGrupo> enRiesgo) {
}
