package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Notificacion;
import co.edu.unbosque.collegeAdministrator.entity.Usuario;

import java.util.List;


/**
 * Operaciones de notificación a los usuarios.
 */
public interface NotificacionServiceAPI {

    /**
     * Guarda una notificación e intenta enviarla por correo.
     *
     * @param usuario el usuario destinatario
     * @param asunto el asunto de la notificación
     * @param mensaje el contenido de la notificación
     * @return la notificación guardada
     */
    Notificacion notificar(Usuario usuario, String asunto, String mensaje);


    /**
     * Envía un correo sin guardar la notificación; se usa para contenido sensible.
     *
     * @param usuario el usuario destinatario
     * @param asunto el asunto de la notificación
     * @param mensaje el contenido de la notificación
     */
    void enviarSoloCorreo(Usuario usuario, String asunto, String mensaje);

    /**
     * Lista las notificaciones de un usuario.
     *
     * @param idUsuario el identificador del usuario
     * @return las notificaciones, de la más reciente a la más antigua
     */
    List<Notificacion> listarPorUsuario(Integer idUsuario);


    /**
     * Marca como leída una notificación del usuario.
     *
     * @param idNotificacion el identificador de la notificación
     * @param idUsuario el identificador del usuario
     * @return la notificación actualizada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si no existe
     *         o pertenece a otro usuario
     */
    Notificacion marcarLeida(Integer idNotificacion, Integer idUsuario);
}
