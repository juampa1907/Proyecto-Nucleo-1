package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.LimiteInventarioAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;


/**
 * Operaciones sobre los topes de inventario de aulas.
 */
public interface LimiteInventarioAulaServiceAPI extends GenericServiceAPI<LimiteInventarioAula, Integer> {

    /**
     * Crea o actualiza el tope de aulas de un tipo de espacio en una sede.
     *
     * @param tipoEspacio el tipo de espacio del aula
     * @param sede la sede de la universidad
     * @param maximo el número máximo de aulas
     * @return el tope guardado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si falta el tipo o
     *         la sede o el máximo es negativo
     */
    LimiteInventarioAula definirLimite(TipoEspacio tipoEspacio, Sede sede, Integer maximo);
}
