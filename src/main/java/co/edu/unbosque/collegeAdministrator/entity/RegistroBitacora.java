package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * HU-52: quién hizo qué cambio y cuándo.
 */
@Entity
@Table(name = "REGISTRO_BITACORA")
public class RegistroBitacora {

    /** El identificador único del registro. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REGISTRO")
    private Integer idRegistro;

    /** El nombre del usuario que realizó la acción. */
    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    /** La fecha y hora de la acción. */
    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    /** El código de la acción realizada. */
    @Column(name = "ACCION", nullable = false, length = 60)
    private String accion;

    /** El detalle de la acción. */
    @Column(name = "DETALLE", length = 1000)
    private String detalle;

    /**
     * Constructor vacío requerido por JPA.
     */
    public RegistroBitacora() {
    }

    /**
     * Crea un registro con la fecha y hora actuales.
     *
     * @param username el nombre del usuario que realizó la acción
     * @param accion el código de la acción realizada
     * @param detalle el detalle de la acción
     */
    public RegistroBitacora(String username, String accion, String detalle) {
        this.username = username;
        this.accion = accion;
        this.detalle = detalle;
        this.fecha = LocalDateTime.now();
    }

    /**
     * Obtiene el identificador único del registro.
     *
     * @return el identificador único del registro
     */
    public Integer getIdRegistro() {
        return idRegistro;
    }

    /**
     * Obtiene el nombre del usuario que realizó la acción.
     *
     * @return el nombre del usuario que realizó la acción
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtiene la fecha y hora de la acción.
     *
     * @return la fecha y hora de la acción
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Obtiene el código de la acción realizada.
     *
     * @return el código de la acción realizada
     */
    public String getAccion() {
        return accion;
    }

    /**
     * Obtiene el detalle de la acción.
     *
     * @return el detalle de la acción
     */
    public String getDetalle() {
        return detalle;
    }
}
