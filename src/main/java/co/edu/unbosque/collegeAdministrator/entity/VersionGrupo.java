package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad JPA con la copia del docente y las sesiones de un grupo dentro de una versión del horario.
 */
@Entity
@Table(name = "VERSION_GRUPO")
public class VersionGrupo {

    /** El identificador único del registro. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VERSION_GRUPO")
    private Integer idVersionGrupo;

    /** La versión de horario a la que pertenece. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VERSION", nullable = false)
    private VersionHorario version;

    /** El grupo copiado. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    /** El docente que tenía el grupo en esa versión, o {@code null} si no tenía. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCENTE")
    private Docente docente;

    /** Las sesiones que tenía el grupo en esa versión. */
    @OneToMany(mappedBy = "versionGrupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VersionSesion> sesiones = new ArrayList<>();

    /**
     * Constructor vacío requerido por JPA.
     */
    public VersionGrupo() {
    }

    /**
     * Crea una instancia de {@code VersionGrupo} con los datos indicados.
     *
     * @param grupo el grupo copiado
     * @param docente el docente que tenía el grupo en esa versión, o {@code null} si no tenía
     */
    public VersionGrupo(Grupo grupo, Docente docente) {
        this.grupo = grupo;
        this.docente = docente;
    }

    /**
     * Obtiene el identificador único del registro.
     *
     * @return el identificador único del registro
     */
    public Integer getIdVersionGrupo() {
        return idVersionGrupo;
    }

    /**
     * Obtiene la versión de horario a la que pertenece.
     *
     * @return la versión de horario a la que pertenece
     */
    public VersionHorario getVersion() {
        return version;
    }

    /**
     * Establece la versión del registro. Lo invoca {@code VersionHorario} para mantener la relación
     * bidireccional.
     *
     * @param version la versión de horario
     */
    void setVersion(VersionHorario version) {
        this.version = version;
    }

    /**
     * Obtiene el grupo copiado.
     *
     * @return el grupo copiado
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Obtiene el docente que tenía el grupo en esa versión, o {@code null} si no tenía.
     *
     * @return el docente que tenía el grupo en esa versión, o {@code null} si no tenía
     */
    public Docente getDocente() {
        return docente;
    }

    /**
     * Obtiene las sesiones guardadas.
     *
     * @return una vista no modificable de las sesiones
     */
    public List<VersionSesion> getSesiones() {
        return Collections.unmodifiableList(sesiones);
    }

    /**
     * Agrega una sesión y la vincula con este registro.
     *
     * @param sesion la sesión que se agrega
     */
    public void agregarSesion(VersionSesion sesion) {
        sesiones.add(sesion);
        sesion.setVersionGrupo(this);
    }
}
