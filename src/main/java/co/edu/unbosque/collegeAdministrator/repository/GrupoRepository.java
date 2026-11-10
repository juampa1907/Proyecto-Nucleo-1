package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de persistencia de {@code Grupo}.
 */
public interface GrupoRepository extends JpaRepository<Grupo, Integer> {

    /**
     * Busca los grupos de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos del periodo
     */
    List<Grupo> findByPeriodo_IdPeriodo(Integer idPeriodo);

    /**
     * Busca los grupos de un periodo en un estado.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param estado el estado del grupo
     * @return los grupos que cumplen ambos criterios
     */
    List<Grupo> findByPeriodo_IdPeriodoAndEstado(Integer idPeriodo, EstadoGrupo estado);

    /**
     * Busca los grupos de una materia en un periodo y estado.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param idMateria el identificador de la materia
     * @param estado el estado del grupo
     * @return los grupos que cumplen los criterios
     */
    List<Grupo> findByPeriodo_IdPeriodoAndMateria_IdMateriaAndEstado(Integer idPeriodo, Integer idMateria, EstadoGrupo estado);

    /**
     * Busca los grupos de un docente en un periodo.
     *
     * @param idDocente el identificador del docente
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos del docente
     */
    List<Grupo> findByDocente_IdUsuarioAndPeriodo_IdPeriodo(Integer idDocente, Integer idPeriodo);

    /**
     * Indica si ya existe un grupo con ese código, sin distinguir mayúsculas, para la materia y el periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param idMateria el identificador de la materia
     * @param codigo el código del grupo
     * @return {@code true} si ya existe
     */
    boolean existsByPeriodo_IdPeriodoAndMateria_IdMateriaAndCodigoIgnoreCase(Integer idPeriodo, Integer idMateria, String codigo);
}
