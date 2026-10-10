package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICACION")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NOTIFICACION")
    private Integer idNotificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "ASUNTO", nullable = false, length = 150)
    private String asunto;

    @Column(name = "MENSAJE", nullable = false, length = 2000)
    private String mensaje;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "LEIDA", nullable = false)
    private boolean leida;

    @Column(name = "ENVIADA_POR_CORREO", nullable = false)
    private boolean enviadaPorCorreo;

    public Notificacion() {
    }

    public Notificacion(Usuario usuario, String asunto, String mensaje) {
        this.usuario = usuario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public boolean isEnviadaPorCorreo() {
        return enviadaPorCorreo;
    }

    public void setEnviadaPorCorreo(boolean enviadaPorCorreo) {
        this.enviadaPorCorreo = enviadaPorCorreo;
    }
}
