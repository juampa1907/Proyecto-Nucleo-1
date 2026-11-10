package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.DetalleHorario;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

/**
 * Repositorio de persistencia de {@code DetalleHorario}.
 */
public interface DetalleHorarioRepository extends JpaRepository<DetalleHorario, Integer> {

    /**
     * Busca las sesiones de un periodo que ocupan un aula en un intervalo.
     *
     * @param idAula el identificador del aula
     * @param idPeriodo el identificador del periodo académico
     * @param dia el día de la semana
     * @param horaInicio la hora de inicio
     * @param horaFin la hora de fin
     * @return las sesiones que se solapan con el intervalo
     */
    @Query("""
            select d from DetalleHorario d
            where d.aula.idAula = :idAula
              and d.grupo.periodo.idPeriodo = :idPeriodo
              and d.franja.diaSemana = :dia
              and d.franja.horaInicio < :horaFin
              and d.franja.horaFin > :horaInicio
            """)
    List<DetalleHorario> buscarCrucesDeAula(@Param("idAula") Integer idAula,
                                            @Param("idPeriodo") Integer idPeriodo,
                                            @Param("dia") DiaSemana dia,
                                            @Param("horaInicio") LocalTime horaInicio,
                                            @Param("horaFin") LocalTime horaFin);

    /**
     * Busca las sesiones de un periodo que dicta un docente en un intervalo.
     *
     * @param idDocente el identificador del docente
     * @param idPeriodo el identificador del periodo académico
     * @param dia el día de la semana
     * @param horaInicio la hora de inicio
     * @param horaFin la hora de fin
     * @return las sesiones que se solapan con el intervalo
     */
    @Query("""
            select d from DetalleHorario d
            where d.grupo.docente.idUsuario = :idDocente
              and d.grupo.periodo.idPeriodo = :idPeriodo
              and d.franja.diaSemana = :dia
              and d.franja.horaInicio < :horaFin
              and d.franja.horaFin > :horaInicio
            """)
    List<DetalleHorario> buscarCrucesDeDocente(@Param("idDocente") Integer idDocente,
                                               @Param("idPeriodo") Integer idPeriodo,
                                               @Param("dia") DiaSemana dia,
                                               @Param("horaInicio") LocalTime horaInicio,
                                               @Param("horaFin") LocalTime horaFin);

    /**
     * Busca las sesiones de un aula.
     *
     * @param idAula el identificador del aula
     * @return las sesiones asignadas al aula
     */
    List<DetalleHorario> findByAula_IdAula(Integer idAula);

    /**
     * Busca las sesiones de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return las sesiones de los grupos del periodo
     */
    List<DetalleHorario> findByGrupo_Periodo_IdPeriodo(Integer idPeriodo);
}
