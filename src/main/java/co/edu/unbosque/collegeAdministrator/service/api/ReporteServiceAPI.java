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
 * Reportes del documento de requisitos. Cada reporte tiene su versión en {@link TablaReporte} para exportarlo (HU-45).
 * Los filtros son opcionales: null significa "todos".
 */
public interface ReporteServiceAPI {

    /**
     * HU-42: ocupación de aulas por franja, filtrable por edificio (bloque), día y rango de horas.
     */
    List<OcupacionAula> ocupacionAulas(Integer idPeriodo, Bloque bloque, DiaSemana dia, LocalTime desde, LocalTime hasta);

    /**
     * HU-08 / HU-43: carga de cada docente con su indicador, y totales por tipo de vinculación contra el presupuesto.
     */
    ReporteCargaDocente cargaDocente(Integer idPeriodo);

    /**
     * HU-44: grupos abiertos, cerrados y en riesgo de cierre.
     */
    EstadoGrupos estadoGrupos(Integer idPeriodo);

    TablaReporte tablaOcupacionAulas(Integer idPeriodo, Bloque bloque, DiaSemana dia, LocalTime desde, LocalTime hasta);

    TablaReporte tablaCargaDocente(Integer idPeriodo);

    TablaReporte tablaEstadoGrupos(Integer idPeriodo);

    /**
     * Horario completo del periodo (materia, grupo, docente, día, horas, aula), para exportarlo.
     */
    TablaReporte horarioPeriodo(Integer idPeriodo);
}
