package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.TipoVinculacion;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

public interface TipoVinculacionServiceAPI extends GenericServiceAPI<TipoVinculacion, Integer> {

    TipoVinculacion buscarPorNombre(String nombre);
}
