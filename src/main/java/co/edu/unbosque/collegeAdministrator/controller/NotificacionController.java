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
 * Notificaciones del usuario autenticado (HU-39 a HU-41). Cada usuario solo ve las suyas.
 */
@RestController
@RequestMapping("/api/notificaciones")
@PreAuthorize(Permisos.AUTENTICADO)
public class NotificacionController {

    private final NotificacionServiceAPI notificacionService;
    private final UsuarioActual usuarioActual;

    public NotificacionController(NotificacionServiceAPI notificacionService, UsuarioActual usuarioActual) {
        this.notificacionService = notificacionService;
        this.usuarioActual = usuarioActual;
    }

    @GetMapping
    public List<NotificacionResponse> misNotificaciones() {
        return notificacionService.listarPorUsuario(usuarioActual.obtenerIdUsuario()).stream()
                .map(AdministracionMapper::aResponse)
                .toList();
    }

    @PatchMapping("/{id}/leida")
    public NotificacionResponse marcarLeida(@PathVariable Integer id) {
        return AdministracionMapper.aResponse(notificacionService.marcarLeida(id, usuarioActual.obtenerIdUsuario()));
    }
}
