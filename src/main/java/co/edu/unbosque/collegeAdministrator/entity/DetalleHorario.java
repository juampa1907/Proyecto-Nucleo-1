package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa una sesión de clase de un grupo: franja horaria y aula asignada.
 */
@Entity
@Table(name = "DETALLE_HORARIO")
public class DetalleHorario {

    /** El identificador único de la sesión. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_HORARIO")
    private Integer idDetalleHorario;

    /** La franja horaria de la sesión. */
    @Embedded
    private FranjaHoraria franja;

    /** El grupo al que pertenece la sesión. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    /** El aula asignada a la sesión, o {@code null} si aún no tiene. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA")
    private Aula aula;

    // ESP-F-05: los auditorios solo se asignan de forma manual y marcada como excepcional.
    /** Indica si el aula se asignó como uso excepcional (ESP-F-05: los auditorios solo se asignan así). */
    @Column(name = "USO_EXCEPCIONAL", nullable = false)
    private boolean usoExcepcional;

    /**
     * Constructor vacío requerido por JPA.
     */
    public DetalleHorario() {
    }

    /**
     * Crea una instancia de {@code DetalleHorario} con los datos indicados.
     *
     * @param franja la franja horaria de la sesión
     * @param aula el aula asignada a la sesión, o {@code null} si aún no tiene
     * @param usoExcepcional {@code true} si el aula se asignó como uso excepcional (ESP-F-05: los auditorios
     *         solo se asignan así)
     */
    public DetalleHorario(FranjaHoraria franja, Aula aula, boolean usoExcepcional) {
        this.franja = franja;
        this.aula = aula;
        this.usoExcepcional = usoExcepcional;
    }

    /**
     * Indica si el aula se asignó como uso excepcional (ESP-F-05: los auditorios solo se asignan así).
     *
     * @return {@code true} si el aula se asignó como uso excepcional (ESP-F-05: los auditorios solo se
     *         asignan así)
     */
    public boolean isUsoExcepcional() {
        return usoExcepcional;
    }

    /**
     * Obtiene el identificador único de la sesión.
     *
     * @return el identificador único de la sesión
     */
    public Integer getIdDetalleHorario() {
        return idDetalleHorario;
    }

    /**
     * Establece el identificador único de la sesión.
     *
     * @param idDetalleHorario el identificador único de la sesión
     */
    public void setIdDetalleHorario(Integer idDetalleHorario) {
        this.idDetalleHorario = idDetalleHorario;
    }

    /**
     * Obtiene la franja horaria de la sesión.
     *
     * @return la franja horaria de la sesión
     */
    public FranjaHoraria getFranja() {
        return franja;
    }

    /**
     * Establece la franja horaria de la sesión.
     *
     * @param franja la franja horaria de la sesión
     */
    public void setFranja(FranjaHoraria franja) {
        this.franja = franja;
    }

    /**
     * Obtiene el grupo al que pertenece la sesión.
     *
     * @return el grupo al que pertenece la sesión
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Establece el grupo de la sesión. Lo invoca {@code Grupo} para mantener la relación bidireccional.
     *
     * @param grupo el grupo al que pertenece la sesión, o {@code null} al retirarla
     */
    void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    /**
     * Obtiene el aula asignada a la sesión, o {@code null} si aún no tiene.
     *
     * @return el aula asignada a la sesión, o {@code null} si aún no tiene
     */
    public Aula getAula() {
        return aula;
    }

    /**
     * Establece el aula asignada a la sesión, o {@code null} si aún no tiene.
     *
     * @param aula el aula asignada a la sesión, o {@code null} si aún no tiene
     */
    public void setAula(Aula aula) {
        this.aula = aula;
    }
}
