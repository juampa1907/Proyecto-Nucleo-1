package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.reporte.EstadoGrupos;
import co.edu.unbosque.collegeAdministrator.dto.reporte.OcupacionAula;
import co.edu.unbosque.collegeAdministrator.dto.reporte.ReporteCargaDocente;
import co.edu.unbosque.collegeAdministrator.dto.reporte.TablaReporte;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;

import java.time.LocalTime;
import java.util.List;


public interface ReporteServiceAPI {


    List<OcupacionAula> ocupacionAulas(Integer idPeriodo, Bloque bloque, DiaSemana dia, LocalTime desde, LocalTime hasta);


    ReporteCargaDocente cargaDocente(Integer idPeriodo);


    EstadoGrupos estadoGrupos(Integer idPeriodo);

    TablaReporte tablaOcupacionAulas(Integer idPeriodo, Bloque bloque, DiaSemana dia, LocalTime desde, LocalTime hasta);

    TablaReporte tablaCargaDocente(Integer idPeriodo);

    TablaReporte tablaEstadoGrupos(Integer idPeriodo);


    TablaReporte horarioPeriodo(Integer idPeriodo);
}
