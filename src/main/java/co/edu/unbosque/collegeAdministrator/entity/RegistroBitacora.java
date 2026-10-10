package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * HU-52: quién hizo qué cambio y cuándo.
 */
@Entity
@Table(name = "REGISTRO_BITACORA")
public class RegistroBitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REGISTRO")
    private Integer idRegistro;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "ACCION", nullable = false, length = 60)
    private String accion;

    @Column(name = "DETALLE", length = 1000)
    private String detalle;

    public RegistroBitacora() {
    }

    public RegistroBitacora(String username, String accion, String detalle) {
        this.username = username;
        this.accion = accion;
        this.detalle = detalle;
        this.fecha = LocalDateTime.now();
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getAccion() {
        return accion;
    }

    public String getDetalle() {
        return detalle;
    }
}
