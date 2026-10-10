package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;

import java.util.Set;

public record AulaResponse(Integer idAula, String codigo, Integer capacidad, TipoEspacio tipoEspacio, Bloque bloque,
                           Sede sede, Set<CaracteristicaAula> caracteristicas, boolean activa) {
}
