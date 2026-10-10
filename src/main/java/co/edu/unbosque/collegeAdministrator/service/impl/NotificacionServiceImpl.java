package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.Notificacion;
import co.edu.unbosque.collegeAdministrator.entity.Usuario;
import co.edu.unbosque.collegeAdministrator.repository.NotificacionRepository;
import co.edu.unbosque.collegeAdministrator.service.api.NotificacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificacionServiceImpl implements NotificacionServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(NotificacionServiceImpl.class);

    private final NotificacionRepository notificacionRepository;
    private final ObjectProvider<JavaMailSender> mailSender;
    private final boolean correoHabilitado;
    private final String remitente;

    // ObjectProvider: el JavaMailSender solo existe si spring.mail.host está configurado.
    public NotificacionServiceImpl(NotificacionRepository notificacionRepository,
                                   ObjectProvider<JavaMailSender> mailSender,
                                   @Value("${app.notificaciones.correo-habilitado:false}") boolean correoHabilitado,
                                   @Value("${app.notificaciones.remitente:no-reply@unbosque.edu.co}") String remitente) {
        this.notificacionRepository = notificacionRepository;
        this.mailSender = mailSender;
        this.correoHabilitado = correoHabilitado;
        this.remitente = remitente;
    }

    @Override
    public Notificacion notificar(Usuario usuario, String asunto, String mensaje) {
        Notificacion notificacion = new Notificacion(usuario, asunto, mensaje);
        notificacion.setEnviadaPorCorreo(enviarCorreo(usuario.getCorreo(), asunto, mensaje));
        return notificacionRepository.save(notificacion);
    }

    @Override
    public void enviarSoloCorreo(Usuario usuario, String asunto, String mensaje) {
        enviarCorreo(usuario.getCorreo(), asunto, mensaje);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> listarPorUsuario(Integer idUsuario) {
        return notificacionRepository.findByUsuario_IdUsuarioOrderByFechaDesc(idUsuario);
    }

    @Override
    public Notificacion marcarLeida(Integer idNotificacion, Integer idUsuario) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .filter(n -> n.getUsuario().getIdUsuario().equals(idUsuario))
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la notificación " + idNotificacion));
        notificacion.setLeida(true);
        return notificacion;
    }

    // Un fallo de correo se registra en el log y no interrumpe la operación que generó la notificación.
    private boolean enviarCorreo(String destinatario, String asunto, String mensaje) {
        if (!correoHabilitado) {
            return false;
        }
        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null) {
            LOG.warn("Correo habilitado pero spring.mail.host no está configurado; no se envió '{}'", asunto);
            return false;
        }
        try {
            SimpleMailMessage correo = new SimpleMailMessage();
            correo.setFrom(remitente);
            correo.setTo(destinatario);
            correo.setSubject(asunto);
            correo.setText(mensaje);
            sender.send(correo);
            return true;
        } catch (MailException e) {
            LOG.warn("No se pudo enviar el correo '{}' a {}: {}", asunto, destinatario, e.getMessage());
            return false;
        }
    }
}
