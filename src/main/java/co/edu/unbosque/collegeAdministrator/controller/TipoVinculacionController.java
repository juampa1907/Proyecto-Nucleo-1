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
 * Controlador REST de los tipos de vinculación.
 */
@RestController
@RequestMapping("/api/tipos-vinculacion")
public class TipoVinculacionController {

    /** El servicio de tipos de vinculación docente. */
    private final TipoVinculacionServiceAPI tipoVinculacionService;

    /**
     * Crea una instancia de {@code TipoVinculacionController} con las dependencias requeridas.
     *
     * @param tipoVinculacionService el servicio de tipos de vinculación docente
     */
    public TipoVinculacionController(TipoVinculacionServiceAPI tipoVinculacionService) {
        this.tipoVinculacionService = tipoVinculacionService;
    }

    /**
     * Lista los tipos de vinculación.
     *
     * <p>Endpoint: {@code GET /api/tipos-vinculacion}. Acceso: cualquier usuario autenticado.
     *
     * @return todos los tipos de vinculación
     */
    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<TipoVinculacionResponse> listar() {
        return tipoVinculacionService.listar().stream().map(UsuarioMapper::aResponse).toList();
    }

    /**
     * Consulta un tipo de vinculación.
     *
     * <p>Endpoint: {@code GET /api/tipos-vinculacion/{id}}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del tipo de vinculación
     * @return sus datos
     */
    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public TipoVinculacionResponse buscar(@PathVariable Integer id) {
        return UsuarioMapper.aResponse(tipoVinculacionService.buscarPorId(id));
    }

    /**
     * Crea un tipo de vinculación.
     *
     * <p>Endpoint: {@code POST /api/tipos-vinculacion}. Acceso: administrador.
     *
     * @param request los datos validados del tipo de vinculación
     * @return la respuesta 201 con el registro creado
     */
    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<TipoVinculacionResponse> crear(@Valid @RequestBody TipoVinculacionRequest request) {
        TipoVinculacion tipo = tipoVinculacionService.guardar(UsuarioMapper.nuevoTipoVinculacion(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.aResponse(tipo));
    }

    /**
     * Actualiza un tipo de vinculación.
     *
     * <p>Endpoint: {@code PUT /api/tipos-vinculacion/{id}}. Acceso: administrador.
     *
     * @param id el identificador del tipo de vinculación
     * @param request los datos validados del tipo de vinculación
     * @return los datos actualizados
     */
    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public TipoVinculacionResponse actualizar(@PathVariable Integer id, @Valid @RequestBody TipoVinculacionRequest request) {
        TipoVinculacion tipo = tipoVinculacionService.buscarPorId(id);
        UsuarioMapper.aplicar(request, tipo);
        return UsuarioMapper.aResponse(tipoVinculacionService.guardar(tipo));
    }

    /**
     * Elimina un tipo de vinculación.
     *
     * <p>Endpoint: {@code DELETE /api/tipos-vinculacion/{id}}. Acceso: administrador.
     *
     * @param id el identificador del tipo de vinculación
     * @return la respuesta 204
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoVinculacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
