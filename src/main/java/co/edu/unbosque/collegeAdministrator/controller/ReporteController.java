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


/**
 * Controlador REST de los reportes y su exportación.
 */
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    /** Tipo de contenido de los archivos xlsx. */
    private static final MediaType EXCEL =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    /** El servicio que construye los reportes. */
    private final ReporteServiceAPI reporteService;
    /** El servicio que exporta tablas a PDF y Excel. */
    private final ExportacionServiceAPI exportacionService;

    /**
     * Crea una instancia de {@code ReporteController} con las dependencias requeridas.
     *
     * @param reporteService el servicio que construye los reportes
     * @param exportacionService el servicio que exporta tablas a PDF y Excel
     */
    public ReporteController(ReporteServiceAPI reporteService, ExportacionServiceAPI exportacionService) {
        this.reporteService = reporteService;
        this.exportacionService = exportacionService;
    }

    /**
     * Consulta la ocupación de las aulas.
     *
     * <p>Endpoint: {@code GET /api/reportes/ocupacion-aulas}. Acceso: director de programa, decano o
     * administrador.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param bloque el bloque por el que se filtra, o {@code null}
     * @param dia el día por el que se filtra, o {@code null}
     * @param desde la hora inicial, o {@code null}
     * @param hasta la hora final, o {@code null}
     * @return la ocupación de cada aula
     */
    @GetMapping("/ocupacion-aulas")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public List<OcupacionAula> ocupacionAulas(@RequestParam Integer idPeriodo,
                                              @RequestParam(required = false) Bloque bloque,
                                              @RequestParam(required = false) DiaSemana dia,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime desde,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime hasta) {
        return reporteService.ocupacionAulas(idPeriodo, bloque, dia, desde, hasta);
    }

    /**
     * Exporta la ocupación de las aulas.
     *
     * <p>Endpoint: {@code GET /api/reportes/ocupacion-aulas/exportar}. Acceso: director de programa, decano o
     * administrador.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param formato el formato de exportación
     * @param bloque el bloque por el que se filtra, o {@code null}
     * @param dia el día por el que se filtra, o {@code null}
     * @param desde la hora inicial, o {@code null}
     * @param hasta la hora final, o {@code null}
     * @return la respuesta con el archivo generado
     */
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

    /**
     * Consulta la carga docente.
     *
     * <p>Endpoint: {@code GET /api/reportes/carga-docente}. Acceso: director de programa, decano o
     * administrador.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return el reporte de carga docente
     */
    @GetMapping("/carga-docente")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public ReporteCargaDocente cargaDocente(@RequestParam Integer idPeriodo) {
        return reporteService.cargaDocente(idPeriodo);
    }

    /**
     * Exporta la carga docente.
     *
     * <p>Endpoint: {@code GET /api/reportes/carga-docente/exportar}. Acceso: director de programa, decano o
     * administrador.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param formato el formato de exportación
     * @return la respuesta con el archivo generado
     */
    @GetMapping("/carga-docente/exportar")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public ResponseEntity<byte[]> exportarCargaDocente(@RequestParam Integer idPeriodo, @RequestParam FormatoExportacion formato) {
        return archivo(reporteService.tablaCargaDocente(idPeriodo), formato, "carga-docente");
    }

    /**
     * Consulta el estado de los grupos.
     *
     * <p>Endpoint: {@code GET /api/reportes/estado-grupos}. Acceso: director de programa, decano o
     * administrador.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos clasificados por estado
     */
    @GetMapping("/estado-grupos")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public EstadoGrupos estadoGrupos(@RequestParam Integer idPeriodo) {
        return reporteService.estadoGrupos(idPeriodo);
    }

    /**
     * Exporta el estado de los grupos.
     *
     * <p>Endpoint: {@code GET /api/reportes/estado-grupos/exportar}. Acceso: director de programa, decano o
     * administrador.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param formato el formato de exportación
     * @return la respuesta con el archivo generado
     */
    @GetMapping("/estado-grupos/exportar")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public ResponseEntity<byte[]> exportarEstadoGrupos(@RequestParam Integer idPeriodo, @RequestParam FormatoExportacion formato) {
        return archivo(reporteService.tablaEstadoGrupos(idPeriodo), formato, "estado-grupos");
    }

    /**
     * Consulta el horario del periodo.
     *
     * <p>Endpoint: {@code GET /api/reportes/horario}. Acceso: cualquier usuario autenticado.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return la tabla del horario
     */
    @GetMapping("/horario")
    @PreAuthorize(Permisos.AUTENTICADO)
    public TablaReporte horario(@RequestParam Integer idPeriodo) {
        return reporteService.horarioPeriodo(idPeriodo);
    }

    /**
     * Exporta el horario del periodo.
     *
     * <p>Endpoint: {@code GET /api/reportes/horario/exportar}. Acceso: cualquier usuario autenticado.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param formato el formato de exportación
     * @return la respuesta con el archivo generado
     */
    @GetMapping("/horario/exportar")
    @PreAuthorize(Permisos.AUTENTICADO)
    public ResponseEntity<byte[]> exportarHorario(@RequestParam Integer idPeriodo, @RequestParam FormatoExportacion formato) {
        return archivo(reporteService.horarioPeriodo(idPeriodo), formato, "horario");
    }

    /**
     * Genera la descarga de una tabla en el formato solicitado.
     *
     * @param tabla la tabla
     * @param formato el formato de exportación
     * @param nombreBase el nombre del archivo sin extensión
     * @return la respuesta con el archivo adjunto
     */
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
