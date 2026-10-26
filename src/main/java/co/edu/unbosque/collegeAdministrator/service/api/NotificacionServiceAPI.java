package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Notificacion;
import co.edu.unbosque.collegeAdministrator.entity.Usuario;

import java.util.List;


public interface NotificacionServiceAPI {

    Notificacion notificar(Usuario usuario, String asunto, String mensaje);


    void enviarSoloCorreo(Usuario usuario, String asunto, String mensaje);

    List<Notificacion> listarPorUsuario(Integer idUsuario);


    Notificacion marcarLeida(Integer idNotificacion, Integer idUsuario);
}
