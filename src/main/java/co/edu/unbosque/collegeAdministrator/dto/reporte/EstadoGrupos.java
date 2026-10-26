package co.edu.unbosque.collegeAdministrator.dto.reporte;

import java.util.List;


public record EstadoGrupos(List<ResumenGrupo> abiertos, List<ResumenGrupo> cerrados, List<ResumenGrupo> enRiesgo) {
}
