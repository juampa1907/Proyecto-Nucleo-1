package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "INSCRIPCION_MATERIA", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_ESTUDIANTE", "ID_GRUPO"}))
public class InscripcionMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Integer idInscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTUDIANTE", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoMateria estado;

    @Column(name = "NOTA_FINAL", precision = 3, scale = 2)
    private BigDecimal notaFinal;

    public InscripcionMateria() {
    }

    public InscripcionMateria(Grupo grupo, EstadoMateria estado) {
        this.grupo = grupo;
        this.estado = estado;
    }

    public Integer getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Integer idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public EstadoMateria getEstado() {
        return estado;
    }

    public void setEstado(EstadoMateria estado) {
        this.estado = estado;
    }

    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(BigDecimal notaFinal) {
        this.notaFinal = notaFinal;
    }
}
