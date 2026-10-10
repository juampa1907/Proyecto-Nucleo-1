package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;

public record LimiteInventarioResponse(Integer idLimite, TipoEspacio tipoEspacio, Sede sede, Integer maximo) {
}
