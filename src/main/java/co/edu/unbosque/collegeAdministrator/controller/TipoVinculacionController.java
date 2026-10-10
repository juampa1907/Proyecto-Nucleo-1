package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.UsuarioMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.TipoVinculacionRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.TipoVinculacionResponse;
import co.edu.unbosque.collegeAdministrator.entity.TipoVinculacion;
import co.edu.unbosque.collegeAdministrator.service.api.TipoVinculacionServiceAPI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HU-06 / RF-31: tipos de vinculación parametrizables (tiempo completo, 3/4, 1/2, 1/4).
 */
@RestController
@RequestMapping("/api/tipos-vinculacion")
public class TipoVinculacionController {

    private final TipoVinculacionServiceAPI tipoVinculacionService;

    public TipoVinculacionController(TipoVinculacionServiceAPI tipoVinculacionService) {
        this.tipoVinculacionService = tipoVinculacionService;
    }

    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<TipoVinculacionResponse> listar() {
        return tipoVinculacionService.listar().stream().map(UsuarioMapper::aResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public TipoVinculacionResponse buscar(@PathVariable Integer id) {
        return UsuarioMapper.aResponse(tipoVinculacionService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<TipoVinculacionResponse> crear(@Valid @RequestBody TipoVinculacionRequest request) {
        TipoVinculacion tipo = tipoVinculacionService.guardar(UsuarioMapper.nuevoTipoVinculacion(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.aResponse(tipo));
    }

    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public TipoVinculacionResponse actualizar(@PathVariable Integer id, @Valid @RequestBody TipoVinculacionRequest request) {
        TipoVinculacion tipo = tipoVinculacionService.buscarPorId(id);
        UsuarioMapper.aplicar(request, tipo);
        return UsuarioMapper.aResponse(tipoVinculacionService.guardar(tipo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoVinculacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
