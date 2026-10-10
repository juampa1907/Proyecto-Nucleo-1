package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;
import java.util.Set;

public interface AulaServiceAPI extends GenericServiceAPI<Aula, Integer> {

    List<Aula> listarActivas();

    List<Aula> listarPorBloque(Bloque bloque);

    List<Aula> listarPorBloqueYTipo(Bloque bloque, TipoEspacio tipoEspacio);

    Aula desactivar(Integer idAula);

    /**
     * @param requisitos opcional: vacío o null si la materia no exige ninguna característica.
     * @param sede       opcional: null para buscar en todas las sedes.
     */
    List<Aula> sugerirAulas(Integer capacidadMinima, Set<CaracteristicaAula> requisitos, Sede sede);
}
