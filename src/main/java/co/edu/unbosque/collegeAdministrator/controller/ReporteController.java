package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.reporte.EstadoGrupos;
import co.edu.unbosque.collegeAdministrator.dto.reporte.OcupacionAula;
import co.edu.unbosque.collegeAdministrator.dto.reporte.ReporteCargaDocente;
import co.edu.unbosque.collegeAdministrator.dto.reporte.TablaReporte;
import co.edu.unbosque.collegeAdministrator.dto.request.FormatoExportacion;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import co.edu.unbosque.collegeAdministrator.service.api.ExportacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ReporteServiceAPI;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private static final MediaType EXCEL =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final ReporteServiceAPI reporteService;
    private final ExportacionServiceAPI exportacionService;

    public ReporteController(ReporteServiceAPI reporteService, ExportacionServiceAPI exportacionService) {
        this.reporteService = reporteService;
        this.exportacionService = exportacionService;
    }

    @GetMapping("/ocupacion-aulas")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public List<OcupacionAula> ocupacionAulas(@RequestParam Integer idPeriodo,
                                              @RequestParam(required = false) Bloque bloque,
                                              @RequestParam(required = false) DiaSemana dia,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime desde,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime hasta) {
        return reporteService.ocupacionAulas(idPeriodo, bloque, dia, desde, hasta);
    }

    @GetMapping("/ocupacion-aulas/exportar")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public ResponseEntity<byte[]> exportarOcupacionAulas(@RequestParam Integer idPeriodo,
                                                         @RequestParam FormatoExportacion formato,
                                                         @RequestParam(required = false) Bloque bloque,
                                                         @RequestParam(required = false) DiaSemana dia,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime desde,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime hasta) {
        return archivo(reporteService.tablaOcupacionAulas(idPeriodo, bloque, dia, desde, hasta), formato, "ocupacion-aulas");
    }

    @GetMapping("/carga-docente")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public ReporteCargaDocente cargaDocente(@RequestParam Integer idPeriodo) {
        return reporteService.cargaDocente(idPeriodo);
    }

    @GetMapping("/carga-docente/exportar")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public ResponseEntity<byte[]> exportarCargaDocente(@RequestParam Integer idPeriodo, @RequestParam FormatoExportacion formato) {
        return archivo(reporteService.tablaCargaDocente(idPeriodo), formato, "carga-docente");
    }

    @GetMapping("/estado-grupos")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public EstadoGrupos estadoGrupos(@RequestParam Integer idPeriodo) {
        return reporteService.estadoGrupos(idPeriodo);
    }

    @GetMapping("/estado-grupos/exportar")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public ResponseEntity<byte[]> exportarEstadoGrupos(@RequestParam Integer idPeriodo, @RequestParam FormatoExportacion formato) {
        return archivo(reporteService.tablaEstadoGrupos(idPeriodo), formato, "estado-grupos");
    }

    @GetMapping("/horario")
    @PreAuthorize(Permisos.AUTENTICADO)
    public TablaReporte horario(@RequestParam Integer idPeriodo) {
        return reporteService.horarioPeriodo(idPeriodo);
    }

    @GetMapping("/horario/exportar")
    @PreAuthorize(Permisos.AUTENTICADO)
    public ResponseEntity<byte[]> exportarHorario(@RequestParam Integer idPeriodo, @RequestParam FormatoExportacion formato) {
        return archivo(reporteService.horarioPeriodo(idPeriodo), formato, "horario");
    }

    private ResponseEntity<byte[]> archivo(TablaReporte tabla, FormatoExportacion formato, String nombreBase) {
        boolean pdf = formato == FormatoExportacion.PDF;
        byte[] contenido = pdf ? exportacionService.exportarPdf(tabla) : exportacionService.exportarExcel(tabla);
        String nombre = nombreBase + (pdf ? ".pdf" : ".xlsx");
        return ResponseEntity.ok()
                .contentType(pdf ? MediaType.APPLICATION_PDF : EXCEL)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(nombre).build().toString())
                .body(contenido);
    }
}
