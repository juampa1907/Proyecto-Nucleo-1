package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Notificacion;
import co.edu.unbosque.collegeAdministrator.entity.Usuario;

import java.util.List;

/**
 * HU-39, HU-40, HU-41: notificaciones guardadas en la base de datos y, si está configurado, enviadas por correo.
 */
public interface NotificacionServiceAPI {

    Notificacion notificar(Usuario usuario, String asunto, String mensaje);

    /**
     * Solo correo, sin guardar en la base de datos: para contenido sensible como el enlace de recuperación (HU-03).
     */
    void enviarSoloCorreo(Usuario usuario, String asunto, String mensaje);

    List<Notificacion> listarPorUsuario(Integer idUsuario);

    /**
     * HU-53: solo el destinatario puede marcar su notificación como leída.
     */
    Notificacion marcarLeida(Integer idNotificacion, Integer idUsuario);
}
