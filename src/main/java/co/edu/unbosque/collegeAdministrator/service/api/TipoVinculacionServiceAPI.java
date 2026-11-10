package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.TipoVinculacion;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

/**
 * Operaciones de negocio sobre los tipos de vinculación docente.
 */
public interface TipoVinculacionServiceAPI extends GenericServiceAPI<TipoVinculacion, Integer> {

    /**
     * Busca un tipo de vinculación por nombre.
     *
     * @param nombre el nombre buscado
     * @return el tipo de vinculación encontrado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si no existe
     */
    TipoVinculacion buscarPorNombre(String nombre);
}
