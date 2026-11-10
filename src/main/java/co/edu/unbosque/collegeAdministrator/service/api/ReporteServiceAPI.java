package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.reporte.EstadoGrupos;
import co.edu.unbosque.collegeAdministrator.dto.reporte.OcupacionAula;
import co.edu.unbosque.collegeAdministrator.dto.reporte.ReporteCargaDocente;
import co.edu.unbosque.collegeAdministrator.dto.reporte.TablaReporte;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;

import java.time.LocalTime;
import java.util.List;


/**
 * Generación de reportes académicos y administrativos.
 */
public interface ReporteServiceAPI {


    /**
     * Calcula la ocupación de las aulas activas en un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param bloque el bloque por el que se filtra, o {@code null} para todos
     * @param dia el día por el que se filtra, o {@code null} para todos
     * @param desde la hora inicial de la ventana, o {@code null} para el inicio del día
     * @param hasta la hora final de la ventana, o {@code null} para el fin del día
     * @return la ocupación de cada aula, ordenada por bloque y código
     */
    List<OcupacionAula> ocupacionAulas(Integer idPeriodo, Bloque bloque, DiaSemana dia, LocalTime desde, LocalTime hasta);


    /**
     * Calcula la carga de cada docente y los totales por vinculación.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return el reporte de carga docente
     */
    ReporteCargaDocente cargaDocente(Integer idPeriodo);


    /**
     * Clasifica los grupos de un periodo según su estado.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos abiertos, cerrados y en riesgo
     */
    EstadoGrupos estadoGrupos(Integer idPeriodo);

    /**
     * Construye la tabla del reporte de ocupación de aulas.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param bloque el bloque por el que se filtra, o {@code null} para todos
     * @param dia el día por el que se filtra, o {@code null} para todos
     * @param desde la hora inicial de la ventana, o {@code null} para el inicio del día
     * @param hasta la hora final de la ventana, o {@code null} para el fin del día
     * @return la tabla lista para exportar
     */
    TablaReporte tablaOcupacionAulas(Integer idPeriodo, Bloque bloque, DiaSemana dia, LocalTime desde, LocalTime hasta);

    /**
     * Construye la tabla del reporte de carga docente.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return la tabla lista para exportar
     */
    TablaReporte tablaCargaDocente(Integer idPeriodo);

    /**
     * Construye la tabla del reporte de estado de grupos.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return la tabla lista para exportar
     */
    TablaReporte tablaEstadoGrupos(Integer idPeriodo);


    /**
     * Construye la tabla con el horario completo de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return la tabla del horario
     */
    TablaReporte horarioPeriodo(Integer idPeriodo);
}
