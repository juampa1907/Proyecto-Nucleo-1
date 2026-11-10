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
 * Controlador REST de los topes de inventario de aulas.
 */
@RestController
@RequestMapping("/api/limites-inventario")
@PreAuthorize(Permisos.ADMIN)
public class LimiteInventarioController {

    /** El servicio de topes de inventario de aulas. */
    private final LimiteInventarioAulaServiceAPI limiteInventarioAulaService;

    /**
     * Crea una instancia de {@code LimiteInventarioController} con las dependencias requeridas.
     *
     * @param limiteInventarioAulaService el servicio de topes de inventario de aulas
     */
    public LimiteInventarioController(LimiteInventarioAulaServiceAPI limiteInventarioAulaService) {
        this.limiteInventarioAulaService = limiteInventarioAulaService;
    }

    /**
     * Lista los topes registrados.
     *
     * <p>Endpoint: {@code GET /api/limites-inventario}. Acceso: administrador.
     *
     * @return los topes de inventario
     */
    @GetMapping
    public List<LimiteInventarioResponse> listar() {
        return limiteInventarioAulaService.listar().stream().map(AdministracionMapper::aResponse).toList();
    }

    /**
     * Crea o actualiza un tope.
     *
     * <p>Endpoint: {@code PUT /api/limites-inventario}. Acceso: administrador.
     *
     * @param request los datos validados del tope
     * @return el tope guardado
     */
    @PutMapping
    public LimiteInventarioResponse definir(@Valid @RequestBody LimiteInventarioRequest request) {
        return AdministracionMapper.aResponse(
                limiteInventarioAulaService.definirLimite(request.tipoEspacio(), request.sede(), request.maximo()));
    }
}
