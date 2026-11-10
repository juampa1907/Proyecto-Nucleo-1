package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;
import java.util.Set;

/**
 * Operaciones de negocio sobre las aulas.
 */
public interface AulaServiceAPI extends GenericServiceAPI<Aula, Integer> {

    /**
     * Lista las aulas activas.
     *
     * @return las aulas activas
     */
    List<Aula> listarActivas();

    /**
     * Lista las aulas activas de un bloque.
     *
     * @param bloque el bloque o edificio
     * @return las aulas activas del bloque
     */
    List<Aula> listarPorBloque(Bloque bloque);

    /**
     * Lista las aulas activas de un bloque y tipo de espacio.
     *
     * @param bloque el bloque o edificio
     * @param tipoEspacio el tipo de espacio del aula
     * @return las aulas que cumplen ambos criterios
     */
    List<Aula> listarPorBloqueYTipo(Bloque bloque, TipoEspacio tipoEspacio);

    /**
     * Marca un aula como inactiva.
     *
     * @param idAula el identificador del aula
     * @return el aula actualizada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si el aula no
     *         existe
     */
    Aula desactivar(Integer idAula);

    /**
     * Sugiere aulas activas con capacidad suficiente y las características requeridas; nunca sugiere
     * auditorios.
     *
     * @param capacidadMinima la capacidad mínima requerida
     * @param requisitos las características exigidas, o {@code null} si no hay
     * @param sede la sede de búsqueda, o {@code null} para todas
     * @return las aulas candidatas
     */
    List<Aula> sugerirAulas(Integer capacidadMinima, Set<CaracteristicaAula> requisitos, Sede sede);
}
