package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "GRUPO", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_PERIODO", "ID_MATERIA", "CODIGO"}))
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO")
    private Integer idGrupo;

    @Column(name = "CODIGO", nullable = false, length = 10)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MATERIA", nullable = false)
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODO", nullable = false)
    private PeriodoAcademico periodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCENTE")
    private Docente docente;

    @Column(name = "CUPO_MAXIMO", nullable = false)
    private Integer cupoMaximo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoGrupo estado = EstadoGrupo.ABIERTO;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleHorario> sesiones = new ArrayList<>();

    @OneToMany(mappedBy = "grupo")
    private List<InscripcionMateria> inscripciones = new ArrayList<>();

    public Grupo() {
    }

    public Grupo(String codigo, Materia materia, PeriodoAcademico periodo, Integer cupoMaximo) {
        this.codigo = codigo;
        this.materia = materia;
        this.periodo = periodo;
        this.cupoMaximo = cupoMaximo;
    }

    public PeriodoAcademico getPeriodo() {
        return periodo;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public EstadoGrupo getEstado() {
        return estado;
    }

    public void setEstado(EstadoGrupo estado) {
        this.estado = estado;
    }

    public List<DetalleHorario> getSesiones() {
        return Collections.unmodifiableList(sesiones);
    }

    public void agregarSesion(DetalleHorario sesion) {
        sesiones.add(sesion);
        sesion.setGrupo(this);
    }

    public void quitarSesion(DetalleHorario sesion) {
        sesiones.remove(sesion);
        sesion.setGrupo(null);
    }

    public List<InscripcionMateria> getInscripciones() {
        return Collections.unmodifiableList(inscripciones);
    }

    public double calcularHorasSemanales() {
        return sesiones.stream()
                .mapToDouble(sesion -> sesion.getFranja().calcularHoras())
                .sum();
    }
}
