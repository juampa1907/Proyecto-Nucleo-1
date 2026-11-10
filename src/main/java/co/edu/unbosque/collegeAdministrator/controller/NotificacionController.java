package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.AdministracionMapper;
import co.edu.unbosque.collegeAdministrator.dto.response.NotificacionResponse;
import co.edu.unbosque.collegeAdministrator.security.UsuarioActual;
import co.edu.unbosque.collegeAdministrator.service.api.NotificacionServiceAPI;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controlador REST de las notificaciones del usuario autenticado.
 */
@RestController
@RequestMapping("/api/notificaciones")
@PreAuthorize(Permisos.AUTENTICADO)
public class NotificacionController {

    /** El servicio de notificaciones a usuarios. */
    private final NotificacionServiceAPI notificacionService;
    /** El componente que da acceso al usuario autenticado. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea una instancia de {@code NotificacionController} con las dependencias requeridas.
     *
     * @param notificacionService el servicio de notificaciones a usuarios
     * @param usuarioActual el componente que da acceso al usuario autenticado
     */
    public NotificacionController(NotificacionServiceAPI notificacionService, UsuarioActual usuarioActual) {
        this.notificacionService = notificacionService;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Lista las notificaciones del usuario autenticado.
     *
     * <p>Endpoint: {@code GET /api/notificaciones}. Acceso: cualquier usuario autenticado.
     *
     * @return las notificaciones, de la más reciente a la más antigua
     */
    @GetMapping
    public List<NotificacionResponse> misNotificaciones() {
        return notificacionService.listarPorUsuario(usuarioActual.obtenerIdUsuario()).stream()
                .map(AdministracionMapper::aResponse)
                .toList();
    }

    /**
     * Marca como leída una notificación propia.
     *
     * <p>Endpoint: {@code PATCH /api/notificaciones/{id}/leida}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador de la notificación
     * @return la notificación actualizada
     */
    @PatchMapping("/{id}/leida")
    public NotificacionResponse marcarLeida(@PathVariable Integer id) {
        return AdministracionMapper.aResponse(notificacionService.marcarLeida(id, usuarioActual.obtenerIdUsuario()));
    }
}
