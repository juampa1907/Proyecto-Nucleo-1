package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * HU-29: copia del horario de un periodo (docente y sesiones de cada grupo) para poder revertir cambios.
 */
@Entity
@Table(name = "VERSION_HORARIO", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_PERIODO", "NUMERO"}))
public class VersionHorario {

    /** El identificador único de la versión. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VERSION")
    private Integer idVersion;

    /** El periodo al que pertenece la versión. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODO", nullable = false)
    private PeriodoAcademico periodo;

    /** El número consecutivo de la versión dentro del periodo. */
    @Column(name = "NUMERO", nullable = false)
    private Integer numero;

    /** La fecha y hora de creación. */
    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    /** El nombre del usuario que generó la versión. */
    @Column(name = "USUARIO", nullable = false, length = 50)
    private String usuario;

    /** La descripción de la versión. */
    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    /** Las copias de los grupos del periodo. */
    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VersionGrupo> grupos = new ArrayList<>();

    /**
     * Constructor vacío requerido por JPA.
     */
    public VersionHorario() {
    }

    /**
     * Crea una versión con la fecha y hora actuales.
     *
     * @param periodo el periodo al que pertenece la versión
     * @param numero el número consecutivo de la versión dentro del periodo
     * @param usuario el nombre del usuario que generó la versión
     * @param descripcion la descripción de la versión
     */
    public VersionHorario(PeriodoAcademico periodo, Integer numero, String usuario, String descripcion) {
        this.periodo = periodo;
        this.numero = numero;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    /**
     * Obtiene el identificador único de la versión.
     *
     * @return el identificador único de la versión
     */
    public Integer getIdVersion() {
        return idVersion;
    }

    /**
     * Obtiene el periodo al que pertenece la versión.
     *
     * @return el periodo al que pertenece la versión
     */
    public PeriodoAcademico getPeriodo() {
        return periodo;
    }

    /**
     * Obtiene el número consecutivo de la versión dentro del periodo.
     *
     * @return el número consecutivo de la versión dentro del periodo
     */
    public Integer getNumero() {
        return numero;
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
     * Obtiene el nombre del usuario que generó la versión.
     *
     * @return el nombre del usuario que generó la versión
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Obtiene la descripción de la versión.
     *
     * @return la descripción de la versión
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene las copias de los grupos.
     *
     * @return una vista no modificable de los grupos
     */
    public List<VersionGrupo> getGrupos() {
        return Collections.unmodifiableList(grupos);
    }

    /**
     * Agrega la copia de un grupo y la vincula con esta versión.
     *
     * @param grupo la copia que se agrega
     */
    public void agregarGrupo(VersionGrupo grupo) {
        grupos.add(grupo);
        grupo.setVersion(this);
    }
}
