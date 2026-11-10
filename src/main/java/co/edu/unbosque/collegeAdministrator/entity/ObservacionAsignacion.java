package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoObservacion;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una observación de un docente sobre la asignación de un grupo.
 */
@Entity
@Table(name = "OBSERVACION_ASIGNACION")
public class ObservacionAsignacion {

    /** El identificador único de la observación. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_OBSERVACION")
    private Integer idObservacion;

    /** El grupo sobre el que se hace la observación. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    /** El docente que registra la observación. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCENTE", nullable = false)
    private Docente docente;

    /** El texto de la observación. */
    @Column(name = "TEXTO", nullable = false, length = 500)
    private String texto;

    /** La fecha y hora de registro. */
    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    /** El estado de la observación. */
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoObservacion estado = EstadoObservacion.PENDIENTE;

    /**
     * Constructor vacío requerido por JPA.
     */
    public ObservacionAsignacion() {
    }

    /**
     * Crea una observación pendiente con la fecha y hora actuales.
     *
     * @param grupo el grupo sobre el que se hace la observación
     * @param docente el docente que registra la observación
     * @param texto el texto de la observación
     */
    public ObservacionAsignacion(Grupo grupo, Docente docente, String texto) {
        this.grupo = grupo;
        this.docente = docente;
        this.texto = texto;
        this.fecha = LocalDateTime.now();
    }

    /**
     * Obtiene el identificador único de la observación.
     *
     * @return el identificador único de la observación
     */
    public Integer getIdObservacion() {
        return idObservacion;
    }

    /**
     * Establece el identificador único de la observación.
     *
     * @param idObservacion el identificador único de la observación
     */
    public void setIdObservacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    /**
     * Obtiene el grupo sobre el que se hace la observación.
     *
     * @return el grupo sobre el que se hace la observación
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Obtiene el docente que registra la observación.
     *
     * @return el docente que registra la observación
     */
    public Docente getDocente() {
        return docente;
    }

    /**
     * Obtiene el texto de la observación.
     *
     * @return el texto de la observación
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Obtiene la fecha y hora de registro.
     *
     * @return la fecha y hora de registro
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Obtiene el estado de la observación.
     *
     * @return el estado de la observación
     */
    public EstadoObservacion getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la observación.
     *
     * @param estado el estado de la observación
     */
    public void setEstado(EstadoObservacion estado) {
        this.estado = estado;
    }
}
