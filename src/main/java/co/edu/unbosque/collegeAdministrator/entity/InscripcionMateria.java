package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entidad JPA que representa la inscripción de un estudiante en un grupo. Un estudiante solo puede
 * inscribirse una vez en cada grupo.
 */
@Entity
@Table(name = "INSCRIPCION_MATERIA", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_ESTUDIANTE", "ID_GRUPO"}))
public class InscripcionMateria {

    /** El identificador único de la inscripción. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Integer idInscripcion;

    /** El estudiante inscrito. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTUDIANTE", nullable = false)
    private Estudiante estudiante;

    /** El grupo en el que se inscribió el estudiante. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    /** El estado de la materia inscrita. */
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoMateria estado;

    /** La nota final de la materia, o {@code null} si aún no se ha registrado. */
    @Column(name = "NOTA_FINAL", precision = 3, scale = 2)
    private BigDecimal notaFinal;

    /**
     * Constructor vacío requerido por JPA.
     */
    public InscripcionMateria() {
    }

    /**
     * Crea una instancia de {@code InscripcionMateria} con los datos indicados.
     *
     * @param grupo el grupo en el que se inscribió el estudiante
     * @param estado el estado de la materia inscrita
     */
    public InscripcionMateria(Grupo grupo, EstadoMateria estado) {
        this.grupo = grupo;
        this.estado = estado;
    }

    /**
     * Obtiene el identificador único de la inscripción.
     *
     * @return el identificador único de la inscripción
     */
    public Integer getIdInscripcion() {
        return idInscripcion;
    }

    /**
     * Establece el identificador único de la inscripción.
     *
     * @param idInscripcion el identificador único de la inscripción
     */
    public void setIdInscripcion(Integer idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    /**
     * Obtiene el estudiante inscrito.
     *
     * @return el estudiante inscrito
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }

    /**
     * Establece el estudiante de la inscripción. Lo invoca {@code Estudiante} para mantener la relación
     * bidireccional.
     *
     * @param estudiante el estudiante inscrito, o {@code null} al retirar la inscripción
     */
    void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    /**
     * Obtiene el grupo en el que se inscribió el estudiante.
     *
     * @return el grupo en el que se inscribió el estudiante
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Establece el grupo en el que se inscribió el estudiante.
     *
     * @param grupo el grupo en el que se inscribió el estudiante
     */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    /**
     * Obtiene el estado de la materia inscrita.
     *
     * @return el estado de la materia inscrita
     */
    public EstadoMateria getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la materia inscrita.
     *
     * @param estado el estado de la materia inscrita
     */
    public void setEstado(EstadoMateria estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la nota final de la materia, o {@code null} si aún no se ha registrado.
     *
     * @return la nota final de la materia, o {@code null} si aún no se ha registrado
     */
    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    /**
     * Establece la nota final de la materia, o {@code null} si aún no se ha registrado.
     *
     * @param notaFinal la nota final de la materia, o {@code null} si aún no se ha registrado
     */
    public void setNotaFinal(BigDecimal notaFinal) {
        this.notaFinal = notaFinal;
    }
}
