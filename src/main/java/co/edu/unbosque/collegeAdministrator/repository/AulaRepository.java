package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Repositorio de persistencia de {@code Aula}.
 */
public interface AulaRepository extends JpaRepository<Aula, Integer> {

    /**
     * Busca las aulas activas.
     *
     * @return las aulas activas
     */
    List<Aula> findByActivaTrue();

    /**
     * Busca las aulas activas de un bloque.
     *
     * @param bloque el bloque o edificio
     * @return las aulas activas del bloque
     */
    List<Aula> findByBloqueAndActivaTrue(Bloque bloque);

    /**
     * Busca las aulas activas de un bloque y tipo de espacio.
     *
     * @param bloque el bloque o edificio
     * @param tipoEspacio el tipo de espacio del aula
     * @return las aulas que cumplen ambos criterios
     */
    List<Aula> findByBloqueAndTipoEspacioAndActivaTrue(Bloque bloque, TipoEspacio tipoEspacio);

    /**
     * Cuenta las aulas de un tipo de espacio en un conjunto de bloques, sin importar si están activas.
     *
     * @param tipoEspacio el tipo de espacio del aula
     * @param bloques los bloques considerados
     * @return la cantidad de aulas
     */
    long countByTipoEspacioAndBloqueIn(TipoEspacio tipoEspacio, Collection<Bloque> bloques);

    /**
     * Busca aulas activas con capacidad suficiente, excluyendo un tipo de espacio.
     *
     * @param capacidadMinima la capacidad mínima requerida
     * @param tipoExcluido el tipo de espacio excluido
     * @param bloques los bloques considerados
     * @return las aulas candidatas
     */
    List<Aula> findByActivaTrueAndCapacidadGreaterThanEqualAndTipoEspacioNotAndBloqueIn(Integer capacidadMinima,
                                                                                         TipoEspacio tipoExcluido,
                                                                                         Collection<Bloque> bloques);
}
