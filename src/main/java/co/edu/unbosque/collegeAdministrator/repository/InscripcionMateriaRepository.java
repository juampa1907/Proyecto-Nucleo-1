package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionMateriaRepository extends JpaRepository<InscripcionMateria, Integer> {

    long countByGrupo_IdGrupo(Integer idGrupo);

    boolean existsByEstudiante_IdUsuarioAndGrupo_IdGrupo(Integer idEstudiante, Integer idGrupo);

    List<InscripcionMateria> findByEstudiante_IdUsuarioAndEstado(Integer idEstudiante, EstadoMateria estado);

    List<InscripcionMateria> findByGrupo_IdGrupo(Integer idGrupo);

    List<InscripcionMateria> findByGrupo_IdGrupoAndEstado(Integer idGrupo, EstadoMateria estado);
}
