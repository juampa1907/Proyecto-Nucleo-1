package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.AcademicoMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.ParametrosRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.ParametrosResponse;
import co.edu.unbosque.collegeAdministrator.service.api.ParametrosAcademicosServiceAPI;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controlador REST de los parámetros académicos.
 */
@RestController
@RequestMapping("/api/parametros")
public class ParametrosController {

    /** El servicio de parámetros académicos institucionales. */
    private final ParametrosAcademicosServiceAPI parametrosAcademicosService;

    /**
     * Crea una instancia de {@code ParametrosController} con las dependencias requeridas.
     *
     * @param parametrosAcademicosService el servicio de parámetros académicos institucionales
     */
    public ParametrosController(ParametrosAcademicosServiceAPI parametrosAcademicosService) {
        this.parametrosAcademicosService = parametrosAcademicosService;
    }

    /**
     * Consulta los parámetros vigentes.
     *
     * <p>Endpoint: {@code GET /api/parametros}. Acceso: cualquier usuario autenticado.
     *
     * @return los parámetros académicos
     */
    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public ParametrosResponse obtener() {
        return AcademicoMapper.aResponse(parametrosAcademicosService.obtener());
    }

    /**
     * Actualiza los parámetros.
     *
     * <p>Endpoint: {@code PUT /api/parametros}. Acceso: administrador.
     *
     * @param request los datos validados de los parámetros
     * @return los parámetros guardados
     */
    @PutMapping
    @PreAuthorize(Permisos.ADMIN)
    public ParametrosResponse actualizar(@Valid @RequestBody ParametrosRequest request) {
        return AcademicoMapper.aResponse(parametrosAcademicosService.actualizar(AcademicoMapper.aParametros(request)));
    }
}
