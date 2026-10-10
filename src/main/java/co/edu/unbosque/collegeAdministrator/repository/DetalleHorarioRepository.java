package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.DetalleHorario;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface DetalleHorarioRepository extends JpaRepository<DetalleHorario, Integer> {

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

    List<DetalleHorario> findByAula_IdAula(Integer idAula);

    List<DetalleHorario> findByGrupo_Periodo_IdPeriodo(Integer idPeriodo);
}
