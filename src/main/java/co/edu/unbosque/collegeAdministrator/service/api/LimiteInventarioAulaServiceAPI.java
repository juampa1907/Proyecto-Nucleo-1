package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.LimiteInventarioAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;


public interface LimiteInventarioAulaServiceAPI extends GenericServiceAPI<LimiteInventarioAula, Integer> {

    LimiteInventarioAula definirLimite(TipoEspacio tipoEspacio, Sede sede, Integer maximo);
}
