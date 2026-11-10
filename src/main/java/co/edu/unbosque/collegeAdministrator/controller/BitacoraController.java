package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.AdministracionMapper;
import co.edu.unbosque.collegeAdministrator.dto.response.BitacoraResponse;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Controlador REST de consulta de la bitácora (HU-52).
 */
@RestController
@RequestMapping("/api/bitacora")
@PreAuthorize(Permisos.ADMIN)
public class BitacoraController {

    /** Días hacia atrás que se consultan si no se indica fecha inicial. */
    private static final int DIAS_POR_DEFECTO = 7;

    /** El servicio de auditoría donde se registran las acciones. */
    private final BitacoraServiceAPI bitacoraService;

    /**
     * Crea una instancia de {@code BitacoraController} con las dependencias requeridas.
     *
     * @param bitacoraService el servicio de auditoría donde se registran las acciones
     */
    public BitacoraController(BitacoraServiceAPI bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    /**
     * Consulta la bitácora; sin fechas, usa los últimos días hasta el momento actual.
     *
     * <p>Endpoint: {@code GET /api/bitacora}. Acceso: administrador.
     *
     * @param desde la fecha inicial, o {@code null}
     * @param hasta la fecha final, o {@code null}
     * @param username el usuario por el que se filtra, o {@code null}
     * @return los registros encontrados
     */
    @GetMapping
    public List<BitacoraResponse> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam(required = false) String username) {
        LocalDateTime fin = hasta == null ? LocalDateTime.now() : hasta;
        LocalDateTime inicio = desde == null ? fin.minusDays(DIAS_POR_DEFECTO) : desde;
        return bitacoraService.listar(inicio, fin, username).stream().map(AdministracionMapper::aResponse).toList();
    }
}
