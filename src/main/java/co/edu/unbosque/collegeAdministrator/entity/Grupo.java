package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad JPA que representa un grupo de una materia en un periodo, con su docente, cupo, sesiones e
 * inscripciones. La combinación de periodo, materia y código es única.
 */
@Entity
@Table(name = "GRUPO", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_PERIODO", "ID_MATERIA", "CODIGO"}))
public class Grupo {

    /** El identificador único del grupo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO")
    private Integer idGrupo;

    /** El código del grupo. */
    @Column(name = "CODIGO", nullable = false, length = 10)
    private String codigo;

    /** La materia del grupo. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MATERIA", nullable = false)
    private Materia materia;

    /** El periodo académico del grupo. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODO", nullable = false)
    private PeriodoAcademico periodo;

    /** El docente asignado, o {@code null} si aún no tiene. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCENTE")
    private Docente docente;

    /** El cupo máximo de estudiantes. */
    @Column(name = "CUPO_MAXIMO", nullable = false)
    private Integer cupoMaximo;

    /** El estado del grupo. */
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoGrupo estado = EstadoGrupo.ABIERTO;

    /** Las sesiones semanales del grupo. */
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleHorario> sesiones = new ArrayList<>();

    /** Las inscripciones de estudiantes en el grupo. */
    @OneToMany(mappedBy = "grupo")
    private List<InscripcionMateria> inscripciones = new ArrayList<>();

    /**
     * Constructor vacío requerido por JPA.
     */
    public Grupo() {
    }

    /**
     * Crea una instancia de {@code Grupo} con los datos indicados.
     *
     * @param codigo el código del grupo
     * @param materia la materia del grupo
     * @param periodo el periodo académico del grupo
     * @param cupoMaximo el cupo máximo de estudiantes
     */
    public Grupo(String codigo, Materia materia, PeriodoAcademico periodo, Integer cupoMaximo) {
        this.codigo = codigo;
        this.materia = materia;
        this.periodo = periodo;
        this.cupoMaximo = cupoMaximo;
    }

    /**
     * Obtiene el periodo académico del grupo.
     *
     * @return el periodo académico del grupo
     */
    public PeriodoAcademico getPeriodo() {
        return periodo;
    }

    /**
     * Obtiene el identificador único del grupo.
     *
     * @return el identificador único del grupo
     */
    public Integer getIdGrupo() {
        return idGrupo;
    }

    /**
     * Establece el identificador único del grupo.
     *
     * @param idGrupo el identificador único del grupo
     */
    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * Obtiene el código del grupo.
     *
     * @return el código del grupo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código del grupo.
     *
     * @param codigo el código del grupo
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene la materia del grupo.
     *
     * @return la materia del grupo
     */
    public Materia getMateria() {
        return materia;
    }

    /**
     * Establece la materia del grupo.
     *
     * @param materia la materia del grupo
     */
    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    /**
     * Obtiene el docente asignado, o {@code null} si aún no tiene.
     *
     * @return el docente asignado, o {@code null} si aún no tiene
     */
    public Docente getDocente() {
        return docente;
    }

    /**
     * Establece el docente asignado, o {@code null} si aún no tiene.
     *
     * @param docente el docente asignado, o {@code null} si aún no tiene
     */
    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    /**
     * Obtiene el cupo máximo de estudiantes.
     *
     * @return el cupo máximo de estudiantes
     */
    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    /**
     * Establece el cupo máximo de estudiantes.
     *
     * @param cupoMaximo el cupo máximo de estudiantes
     */
    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    /**
     * Obtiene el estado del grupo.
     *
     * @return el estado del grupo
     */
    public EstadoGrupo getEstado() {
        return estado;
    }

    /**
     * Establece el estado del grupo.
     *
     * @param estado el estado del grupo
     */
    public void setEstado(EstadoGrupo estado) {
        this.estado = estado;
    }

    /**
     * Obtiene las sesiones del grupo.
     *
     * @return una vista no modificable de las sesiones
     */
    public List<DetalleHorario> getSesiones() {
        return Collections.unmodifiableList(sesiones);
    }

    /**
     * Agrega una sesión al grupo y la vincula con él.
     *
     * @param sesion la sesión que se agrega
     */
    public void agregarSesion(DetalleHorario sesion) {
        sesiones.add(sesion);
        sesion.setGrupo(this);
    }

    /**
     * Quita una sesión del grupo y elimina su vínculo con él.
     *
     * @param sesion la sesión que se quita
     */
    public void quitarSesion(DetalleHorario sesion) {
        sesiones.remove(sesion);
        sesion.setGrupo(null);
    }

    /**
     * Obtiene las inscripciones del grupo.
     *
     * @return una vista no modificable de las inscripciones
     */
    public List<InscripcionMateria> getInscripciones() {
        return Collections.unmodifiableList(inscripciones);
    }

    /**
     * Calcula las horas semanales programadas para el grupo.
     *
     * @return la suma de la duración de sus sesiones, en horas
     */
    public double calcularHorasSemanales() {
        return sesiones.stream()
                .mapToDouble(sesion -> sesion.getFranja().calcularHoras())
                .sum();
    }
}
