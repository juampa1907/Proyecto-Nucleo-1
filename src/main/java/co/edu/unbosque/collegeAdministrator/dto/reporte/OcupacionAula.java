package co.edu.unbosque.collegeAdministrator.dto.reporte;

import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;


public record OcupacionAula(Integer idAula, String codigo, Bloque bloque, TipoEspacio tipoEspacio,
                            int sesiones, double horasOcupadas, double porcentajeOcupacion) {
}
