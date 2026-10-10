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
 * HU-27, HU-46 a HU-49 / RF-31: parámetros institucionales editables sin tocar código.
 */
@RestController
@RequestMapping("/api/parametros")
public class ParametrosController {

    private final ParametrosAcademicosServiceAPI parametrosAcademicosService;

    public ParametrosController(ParametrosAcademicosServiceAPI parametrosAcademicosService) {
        this.parametrosAcademicosService = parametrosAcademicosService;
    }

    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public ParametrosResponse obtener() {
        return AcademicoMapper.aResponse(parametrosAcademicosService.obtener());
    }

    @PutMapping
    @PreAuthorize(Permisos.ADMIN)
    public ParametrosResponse actualizar(@Valid @RequestBody ParametrosRequest request) {
        return AcademicoMapper.aResponse(parametrosAcademicosService.actualizar(AcademicoMapper.aParametros(request)));
    }
}
