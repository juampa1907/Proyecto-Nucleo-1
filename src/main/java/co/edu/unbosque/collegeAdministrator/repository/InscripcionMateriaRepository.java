package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de persistencia de {@code InscripcionMateria}.
 */
public interface InscripcionMateriaRepository extends JpaRepository<InscripcionMateria, Integer> {

    /**
     * Cuenta las inscripciones de un grupo, sin importar su estado.
     *
     * @param idGrupo el identificador del grupo
     * @return la cantidad de inscripciones
     */
    long countByGrupo_IdGrupo(Integer idGrupo);

    /**
     * Indica si un estudiante ya está inscrito en un grupo.
     *
     * @param idEstudiante el identificador del estudiante
     * @param idGrupo el identificador del grupo
     * @return {@code true} si existe la inscripción
     */
    boolean existsByEstudiante_IdUsuarioAndGrupo_IdGrupo(Integer idEstudiante, Integer idGrupo);

    /**
     * Busca las inscripciones de un estudiante en un estado.
     *
     * @param idEstudiante el identificador del estudiante
     * @param estado el estado de la materia inscrita
     * @return las inscripciones que cumplen ambos criterios
     */
    List<InscripcionMateria> findByEstudiante_IdUsuarioAndEstado(Integer idEstudiante, EstadoMateria estado);

    /**
     * Busca las inscripciones de un grupo.
     *
     * @param idGrupo el identificador del grupo
     * @return las inscripciones del grupo
     */
    List<InscripcionMateria> findByGrupo_IdGrupo(Integer idGrupo);

    /**
     * Busca las inscripciones de un grupo en un estado.
     *
     * @param idGrupo el identificador del grupo
     * @param estado el estado de la materia inscrita
     * @return las inscripciones que cumplen ambos criterios
     */
    List<InscripcionMateria> findByGrupo_IdGrupoAndEstado(Integer idGrupo, EstadoMateria estado);
}
