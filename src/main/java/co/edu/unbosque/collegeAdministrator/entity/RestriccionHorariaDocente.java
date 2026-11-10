package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa una franja en la que un docente no puede dictar clase.
 */
@Entity
@Table(name = "RESTRICCION_HORARIA_DOCENTE")
public class RestriccionHorariaDocente {

    /** El identificador único de la restricción. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESTRICCION")
    private Integer idRestriccion;

    /** La franja bloqueada. */
    @Embedded
    private FranjaHoraria franja;

    /** El docente al que pertenece la restricción. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCENTE", nullable = false)
    private Docente docente;

    /**
     * Constructor vacío requerido por JPA.
     */
    public RestriccionHorariaDocente() {
    }

    /**
     * Crea una instancia de {@code RestriccionHorariaDocente} con los datos indicados.
     *
     * @param franja la franja bloqueada
     */
    public RestriccionHorariaDocente(FranjaHoraria franja) {
        this.franja = franja;
    }

    /**
     * Obtiene el identificador único de la restricción.
     *
     * @return el identificador único de la restricción
     */
    public Integer getIdRestriccion() {
        return idRestriccion;
    }

    /**
     * Establece el identificador único de la restricción.
     *
     * @param idRestriccion el identificador único de la restricción
     */
    public void setIdRestriccion(Integer idRestriccion) {
        this.idRestriccion = idRestriccion;
    }

    /**
     * Obtiene la franja bloqueada.
     *
     * @return la franja bloqueada
     */
    public FranjaHoraria getFranja() {
        return franja;
    }

    /**
     * Establece la franja bloqueada.
     *
     * @param franja la franja bloqueada
     */
    public void setFranja(FranjaHoraria franja) {
        this.franja = franja;
    }

    /**
     * Obtiene el docente al que pertenece la restricción.
     *
     * @return el docente al que pertenece la restricción
     */
    public Docente getDocente() {
        return docente;
    }

    /**
     * Establece el docente de la restricción. Lo invoca {@code Docente} para mantener la relación
     * bidireccional.
     *
     * @param docente el docente de la restricción, o {@code null} al retirarla
     */
    void setDocente(Docente docente) {
        this.docente = docente;
    }
}
