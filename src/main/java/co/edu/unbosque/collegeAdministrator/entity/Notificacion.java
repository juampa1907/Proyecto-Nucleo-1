package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una notificación dirigida a un usuario.
 */
@Entity
@Table(name = "NOTIFICACION")
public class Notificacion {

    /** El identificador único de la notificación. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NOTIFICACION")
    private Integer idNotificacion;

    /** El usuario destinatario. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    /** El asunto de la notificación. */
    @Column(name = "ASUNTO", nullable = false, length = 150)
    private String asunto;

    /** El contenido de la notificación. */
    @Column(name = "MENSAJE", nullable = false, length = 2000)
    private String mensaje;

    /** La fecha y hora de creación. */
    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    /** Indica si la notificación ya fue leída. */
    @Column(name = "LEIDA", nullable = false)
    private boolean leida;

    /** Indica si la notificación se envió también por correo electrónico. */
    @Column(name = "ENVIADA_POR_CORREO", nullable = false)
    private boolean enviadaPorCorreo;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Notificacion() {
    }

    /**
     * Crea una notificación con la fecha y hora actuales.
     *
     * @param usuario el usuario destinatario
     * @param asunto el asunto de la notificación
     * @param mensaje el contenido de la notificación
     */
    public Notificacion(Usuario usuario, String asunto, String mensaje) {
        this.usuario = usuario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    /**
     * Obtiene el identificador único de la notificación.
     *
     * @return el identificador único de la notificación
     */
    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    /**
     * Obtiene el usuario destinatario.
     *
     * @return el usuario destinatario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Obtiene el asunto de la notificación.
     *
     * @return el asunto de la notificación
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Obtiene el contenido de la notificación.
     *
     * @return el contenido de la notificación
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Obtiene la fecha y hora de creación.
     *
     * @return la fecha y hora de creación
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Indica si la notificación ya fue leída.
     *
     * @return {@code true} si la notificación ya fue leída
     */
    public boolean isLeida() {
        return leida;
    }

    /**
     * Establece si la notificación ya fue leída.
     *
     * @param leida {@code true} si la notificación ya fue leída
     */
    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    /**
     * Indica si la notificación se envió también por correo electrónico.
     *
     * @return {@code true} si la notificación se envió también por correo electrónico
     */
    public boolean isEnviadaPorCorreo() {
        return enviadaPorCorreo;
    }

    /**
     * Establece si la notificación se envió también por correo electrónico.
     *
     * @param enviadaPorCorreo {@code true} si la notificación se envió también por correo electrónico
     */
    public void setEnviadaPorCorreo(boolean enviadaPorCorreo) {
        this.enviadaPorCorreo = enviadaPorCorreo;
    }
}
