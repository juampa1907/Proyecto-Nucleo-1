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


/**
 * Controlador REST de los usuarios, sus roles y su contraseña.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    /** El servicio de gestión de usuarios. */
    private final UsuarioServiceAPI usuarioService;
    /** El componente que da acceso al usuario autenticado. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea una instancia de {@code UsuarioController} con las dependencias requeridas.
     *
     * @param usuarioService el servicio de gestión de usuarios
     * @param usuarioActual el componente que da acceso al usuario autenticado
     */
    public UsuarioController(UsuarioServiceAPI usuarioService, UsuarioActual usuarioActual) {
        this.usuarioService = usuarioService;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Consulta los datos del usuario autenticado.
     *
     * <p>Endpoint: {@code GET /api/usuarios/yo}. Acceso: cualquier usuario autenticado.
     *
     * @return los datos del usuario
     */
    @GetMapping("/yo")
    @PreAuthorize(Permisos.AUTENTICADO)
    public UsuarioResponse yo() {
        return UsuarioMapper.aResponse(usuarioService.buscarPorUsername(usuarioActual.obtenerUsername()));
    }

    /**
     * Cambia la contraseña del usuario autenticado.
     *
     * <p>Endpoint: {@code PUT /api/usuarios/yo/password}. Acceso: cualquier usuario autenticado.
     *
     * @param request las contraseñas actual y nueva
     * @return la respuesta 204
     */
    @PutMapping("/yo/password")
    @PreAuthorize(Permisos.AUTENTICADO)
    public ResponseEntity<Void> cambiarPassword(@Valid @RequestBody CambioPasswordRequest request) {
        usuarioService.cambiarPassword(usuarioActual.obtenerIdUsuario(), request.passwordActual(), request.passwordNueva());
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista los usuarios.
     *
     * <p>Endpoint: {@code GET /api/usuarios}. Acceso: administrador.
     *
     * @return todos los usuarios
     */
    @GetMapping
    @PreAuthorize(Permisos.ADMIN)
    public List<UsuarioResponse> listar() {
        return usuarioService.listar().stream().map(UsuarioMapper::aResponse).toList();
    }

    /**
     * Consulta un usuario.
     *
     * <p>Endpoint: {@code GET /api/usuarios/{id}}. Acceso: administrador.
     *
     * @param id el identificador del usuario
     * @return los datos del usuario
     */
    @GetMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public UsuarioResponse buscar(@PathVariable Integer id) {
        return UsuarioMapper.aResponse(usuarioService.buscarPorId(id));
    }

    /**
     * Crea un administrador.
     *
     * <p>Endpoint: {@code POST /api/usuarios/administradores}. Acceso: administrador.
     *
     * @param request los datos validados del administrador
     * @return la respuesta 201 con el usuario creado
     */
    @PostMapping("/administradores")
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<UsuarioResponse> crearAdministrador(@Valid @RequestBody AdministradorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.aResponse(
                usuarioService.crearAdministrador(request.username(), request.correo(), request.password())));
    }

    /**
     * Asigna un rol a un usuario.
     *
     * <p>Endpoint: {@code POST /api/usuarios/{id}/roles/{rol}}. Acceso: administrador.
     *
     * @param id el identificador del usuario
     * @param rol el rol del usuario
     * @return los datos actualizados
     */
    @PostMapping("/{id}/roles/{rol}")
    @PreAuthorize(Permisos.ADMIN)
    public UsuarioResponse asignarRol(@PathVariable Integer id, @PathVariable Rol rol) {
        return UsuarioMapper.aResponse(usuarioService.asignarRol(id, rol));
    }

    /**
     * Retira un rol de un usuario.
     *
     * <p>Endpoint: {@code DELETE /api/usuarios/{id}/roles/{rol}}. Acceso: administrador.
     *
     * @param id el identificador del usuario
     * @param rol el rol del usuario
     * @return los datos actualizados
     */
    @DeleteMapping("/{id}/roles/{rol}")
    @PreAuthorize(Permisos.ADMIN)
    public UsuarioResponse quitarRol(@PathVariable Integer id, @PathVariable Rol rol) {
        return UsuarioMapper.aResponse(usuarioService.quitarRol(id, rol));
    }
}
