package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA con la copia de una sesión de grupo dentro de una versión del horario.
 */
@Entity
@Table(name = "VERSION_SESION")
public class VersionSesion {

    /** El identificador único del registro. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VERSION_SESION")
    private Integer idVersionSesion;

    /** La copia de grupo a la que pertenece la sesión. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VERSION_GRUPO", nullable = false)
    private VersionGrupo versionGrupo;

    /** La franja horaria de la sesión. */
    @Embedded
    private FranjaHoraria franja;

    /** El aula de la sesión, o {@code null} si no tenía. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA")
    private Aula aula;

    /** Indica si el aula se asignó como uso excepcional. */
    @Column(name = "USO_EXCEPCIONAL", nullable = false)
    private boolean usoExcepcional;

    /**
     * Constructor vacío requerido por JPA.
     */
    public VersionSesion() {
    }

    /**
     * Crea una instancia de {@code VersionSesion} con los datos indicados.
     *
     * @param franja la franja horaria de la sesión
     * @param aula el aula de la sesión, o {@code null} si no tenía
     * @param usoExcepcional {@code true} si el aula se asignó como uso excepcional
     */
    public VersionSesion(FranjaHoraria franja, Aula aula, boolean usoExcepcional) {
        this.franja = franja;
        this.aula = aula;
        this.usoExcepcional = usoExcepcional;
    }

    /**
     * Obtiene el identificador único del registro.
     *
     * @return el identificador único del registro
     */
    public Integer getIdVersionSesion() {
        return idVersionSesion;
    }

    /**
     * Establece la copia de grupo de la sesión. Lo invoca {@code VersionGrupo} para mantener la relación
     * bidireccional.
     *
     * @param versionGrupo la copia de grupo
     */
    void setVersionGrupo(VersionGrupo versionGrupo) {
        this.versionGrupo = versionGrupo;
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
     * Obtiene el aula de la sesión, o {@code null} si no tenía.
     *
     * @return el aula de la sesión, o {@code null} si no tenía
     */
    public Aula getAula() {
        return aula;
    }

    /**
     * Indica si el aula se asignó como uso excepcional.
     *
     * @return {@code true} si el aula se asignó como uso excepcional
     */
    public boolean isUsoExcepcional() {
        return usoExcepcional;
    }
}
