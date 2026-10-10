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
 * HU-52: bitácora de cambios. Sin fechas, devuelve los últimos 7 días. Fechas en formato ISO (2026-10-09T08:00:00).
 */
@RestController
@RequestMapping("/api/bitacora")
@PreAuthorize(Permisos.ADMIN)
public class BitacoraController {

    private static final int DIAS_POR_DEFECTO = 7;

    private final BitacoraServiceAPI bitacoraService;

    public BitacoraController(BitacoraServiceAPI bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

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
