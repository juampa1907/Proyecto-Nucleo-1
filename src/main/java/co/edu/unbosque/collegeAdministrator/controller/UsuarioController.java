package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.UsuarioMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.AdministradorRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.CambioPasswordRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.UsuarioResponse;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import co.edu.unbosque.collegeAdministrator.security.UsuarioActual;
import co.edu.unbosque.collegeAdministrator.service.api.UsuarioServiceAPI;
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


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioServiceAPI usuarioService;
    private final UsuarioActual usuarioActual;

    public UsuarioController(UsuarioServiceAPI usuarioService, UsuarioActual usuarioActual) {
        this.usuarioService = usuarioService;
        this.usuarioActual = usuarioActual;
    }

    @GetMapping("/yo")
    @PreAuthorize(Permisos.AUTENTICADO)
    public UsuarioResponse yo() {
        return UsuarioMapper.aResponse(usuarioService.buscarPorUsername(usuarioActual.obtenerUsername()));
    }

    @PutMapping("/yo/password")
    @PreAuthorize(Permisos.AUTENTICADO)
    public ResponseEntity<Void> cambiarPassword(@Valid @RequestBody CambioPasswordRequest request) {
        usuarioService.cambiarPassword(usuarioActual.obtenerIdUsuario(), request.passwordActual(), request.passwordNueva());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize(Permisos.ADMIN)
    public List<UsuarioResponse> listar() {
        return usuarioService.listar().stream().map(UsuarioMapper::aResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public UsuarioResponse buscar(@PathVariable Integer id) {
        return UsuarioMapper.aResponse(usuarioService.buscarPorId(id));
    }

    @PostMapping("/administradores")
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<UsuarioResponse> crearAdministrador(@Valid @RequestBody AdministradorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.aResponse(
                usuarioService.crearAdministrador(request.username(), request.correo(), request.password())));
    }

    @PostMapping("/{id}/roles/{rol}")
    @PreAuthorize(Permisos.ADMIN)
    public UsuarioResponse asignarRol(@PathVariable Integer id, @PathVariable Rol rol) {
        return UsuarioMapper.aResponse(usuarioService.asignarRol(id, rol));
    }

    @DeleteMapping("/{id}/roles/{rol}")
    @PreAuthorize(Permisos.ADMIN)
    public UsuarioResponse quitarRol(@PathVariable Integer id, @PathVariable Rol rol) {
        return UsuarioMapper.aResponse(usuarioService.quitarRol(id, rol));
    }
}
