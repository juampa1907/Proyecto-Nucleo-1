package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "VERSION_SESION")
public class VersionSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VERSION_SESION")
    private Integer idVersionSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VERSION_GRUPO", nullable = false)
    private VersionGrupo versionGrupo;

    @Embedded
    private FranjaHoraria franja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA")
    private Aula aula;

    @Column(name = "USO_EXCEPCIONAL", nullable = false)
    private boolean usoExcepcional;

    public VersionSesion() {
    }

    public VersionSesion(FranjaHoraria franja, Aula aula, boolean usoExcepcional) {
        this.franja = franja;
        this.aula = aula;
        this.usoExcepcional = usoExcepcional;
    }

    public Integer getIdVersionSesion() {
        return idVersionSesion;
    }

    void setVersionGrupo(VersionGrupo versionGrupo) {
        this.versionGrupo = versionGrupo;
    }

    public FranjaHoraria getFranja() {
        return franja;
    }

    public Aula getAula() {
        return aula;
    }

    public boolean isUsoExcepcional() {
        return usoExcepcional;
    }
}
