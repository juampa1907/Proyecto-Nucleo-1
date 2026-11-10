package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

/**
 * Operaciones de negocio sobre las materias y sus requisitos.
 */
public interface MateriaServiceAPI extends GenericServiceAPI<Materia, Integer> {

    /**
     * Busca materias cuyo nombre contenga un texto.
     *
     * @param nombre el texto buscado
     * @return las materias que coinciden
     */
    List<Materia> buscarPorNombre(String nombre);

    /**
     * Agrega un prerrequisito a una materia.
     *
     * @param idMateria el identificador de la materia
     * @param idPrerrequisito el identificador de la materia prerrequisito
     * @return la materia actualizada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la relación es
     *         repetida, entra en conflicto o genera un ciclo directo
     */
    Materia agregarPrerrequisito(Integer idMateria, Integer idPrerrequisito);

    /**
     * Quita un prerrequisito de una materia.
     *
     * @param idMateria el identificador de la materia
     * @param idPrerrequisito el identificador de la materia prerrequisito
     * @return la materia actualizada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la relación no
     *         existe
     */
    Materia quitarPrerrequisito(Integer idMateria, Integer idPrerrequisito);

    /**
     * Agrega un correquisito a una materia; se cursa en el mismo periodo o ya debe estar aprobado (HU-15).
     *
     * @param idMateria el identificador de la materia
     * @param idCorrequisito el identificador de la materia correquisito
     * @return la materia actualizada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la relación es
     *         repetida o entra en conflicto
     */
    Materia agregarCorrequisito(Integer idMateria, Integer idCorrequisito);

    /**
     * Quita un correquisito de una materia.
     *
     * @param idMateria el identificador de la materia
     * @param idCorrequisito el identificador de la materia correquisito
     * @return la materia actualizada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la relación no
     *         existe
     */
    Materia quitarCorrequisito(Integer idMateria, Integer idCorrequisito);

    /**
     * Agrega un requisito especial de aula a una materia (HU-13, RF-15).
     *
     * @param idMateria el identificador de la materia
     * @param requisito la característica exigida
     * @return la materia actualizada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el requisito es
     *         nulo
     */
    Materia agregarRequisitoAula(Integer idMateria, CaracteristicaAula requisito);

    /**
     * Quita un requisito de aula de una materia.
     *
     * @param idMateria el identificador de la materia
     * @param requisito la característica que deja de exigirse
     * @return la materia actualizada
     */
    Materia quitarRequisitoAula(Integer idMateria, CaracteristicaAula requisito);
}
