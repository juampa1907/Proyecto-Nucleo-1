package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

public interface MateriaServiceAPI extends GenericServiceAPI<Materia, Integer> {

    List<Materia> buscarPorNombre(String nombre);

    Materia agregarPrerrequisito(Integer idMateria, Integer idPrerrequisito);

    Materia quitarPrerrequisito(Integer idMateria, Integer idPrerrequisito);

    Materia agregarCorrequisito(Integer idMateria, Integer idCorrequisito);

    Materia quitarCorrequisito(Integer idMateria, Integer idCorrequisito);

    Materia agregarRequisitoAula(Integer idMateria, CaracteristicaAula requisito);

    Materia quitarRequisitoAula(Integer idMateria, CaracteristicaAula requisito);
}
