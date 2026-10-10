package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.AdministracionMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.LimiteInventarioRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.LimiteInventarioResponse;
import co.edu.unbosque.collegeAdministrator.service.api.LimiteInventarioAulaServiceAPI;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ESP-F-02 / ESP-F-04: topes de aulas por tipo y sede según el inventario de la Unidad de Espacios Físicos.
 */
@RestController
@RequestMapping("/api/limites-inventario")
@PreAuthorize(Permisos.ADMIN)
public class LimiteInventarioController {

    private final LimiteInventarioAulaServiceAPI limiteInventarioAulaService;

    public LimiteInventarioController(LimiteInventarioAulaServiceAPI limiteInventarioAulaService) {
        this.limiteInventarioAulaService = limiteInventarioAulaService;
    }

    @GetMapping
    public List<LimiteInventarioResponse> listar() {
        return limiteInventarioAulaService.listar().stream().map(AdministracionMapper::aResponse).toList();
    }

    @PutMapping
    public LimiteInventarioResponse definir(@Valid @RequestBody LimiteInventarioRequest request) {
        return AdministracionMapper.aResponse(
                limiteInventarioAulaService.definirLimite(request.tipoEspacio(), request.sede(), request.maximo()));
    }
}
