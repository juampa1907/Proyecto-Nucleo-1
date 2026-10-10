package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AulaRepository extends JpaRepository<Aula, Integer> {

    List<Aula> findByActivaTrue();

    List<Aula> findByBloqueAndActivaTrue(Bloque bloque);

    List<Aula> findByBloqueAndTipoEspacioAndActivaTrue(Bloque bloque, TipoEspacio tipoEspacio);

    long countByTipoEspacioAndBloqueIn(TipoEspacio tipoEspacio, Collection<Bloque> bloques);

    List<Aula> findByActivaTrueAndCapacidadGreaterThanEqualAndTipoEspacioNotAndBloqueIn(Integer capacidadMinima,
                                                                                         TipoEspacio tipoExcluido,
                                                                                         Collection<Bloque> bloques);
}
