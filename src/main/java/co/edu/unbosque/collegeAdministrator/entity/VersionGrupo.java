package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "VERSION_GRUPO")
public class VersionGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VERSION_GRUPO")
    private Integer idVersionGrupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VERSION", nullable = false)
    private VersionHorario version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCENTE")
    private Docente docente;

    @OneToMany(mappedBy = "versionGrupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VersionSesion> sesiones = new ArrayList<>();

    public VersionGrupo() {
    }

    public VersionGrupo(Grupo grupo, Docente docente) {
        this.grupo = grupo;
        this.docente = docente;
    }

    public Integer getIdVersionGrupo() {
        return idVersionGrupo;
    }

    public VersionHorario getVersion() {
        return version;
    }

    void setVersion(VersionHorario version) {
        this.version = version;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public Docente getDocente() {
        return docente;
    }

    public List<VersionSesion> getSesiones() {
        return Collections.unmodifiableList(sesiones);
    }

    public void agregarSesion(VersionSesion sesion) {
        sesiones.add(sesion);
        sesion.setVersionGrupo(this);
    }
}
