package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Integer> {

    List<Grupo> findByPeriodo_IdPeriodo(Integer idPeriodo);

    List<Grupo> findByPeriodo_IdPeriodoAndEstado(Integer idPeriodo, EstadoGrupo estado);

    List<Grupo> findByPeriodo_IdPeriodoAndMateria_IdMateriaAndEstado(Integer idPeriodo, Integer idMateria, EstadoGrupo estado);

    List<Grupo> findByDocente_IdUsuarioAndPeriodo_IdPeriodo(Integer idDocente, Integer idPeriodo);

    boolean existsByPeriodo_IdPeriodoAndMateria_IdMateriaAndCodigoIgnoreCase(Integer idPeriodo, Integer idMateria, String codigo);
}
