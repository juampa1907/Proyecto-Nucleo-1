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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VERSION")
    private Integer idVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODO", nullable = false)
    private PeriodoAcademico periodo;

    @Column(name = "NUMERO", nullable = false)
    private Integer numero;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "USUARIO", nullable = false, length = 50)
    private String usuario;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VersionGrupo> grupos = new ArrayList<>();

    public VersionHorario() {
    }

    public VersionHorario(PeriodoAcademico periodo, Integer numero, String usuario, String descripcion) {
        this.periodo = periodo;
        this.numero = numero;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public Integer getIdVersion() {
        return idVersion;
    }

    public PeriodoAcademico getPeriodo() {
        return periodo;
    }

    public Integer getNumero() {
        return numero;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<VersionGrupo> getGrupos() {
        return Collections.unmodifiableList(grupos);
    }

    public void agregarGrupo(VersionGrupo grupo) {
        grupos.add(grupo);
        grupo.setVersion(this);
    }
}
