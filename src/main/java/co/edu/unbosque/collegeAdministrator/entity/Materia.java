package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "MATERIA")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIA")
    private Integer idMateria;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "CREDITOS", nullable = false)
    private Integer numeroCreditos;

    @Column(name = "PRERREQUISITOS")
    private List<Materia> prerrequisitos;

    @Column(name = "DOCENTE", nullable = false)
    private Docente docente;

    public Materia() {
    }

    public Materia(Integer idMateria, String nombre, Integer numeroCreditos, List<Materia> prerrequisitos, Docente docente) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.numeroCreditos = numeroCreditos;
        this.prerrequisitos = prerrequisitos;
        this.docente = docente;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumeroCreditos() {
        return numeroCreditos;
    }

    public void setNumeroCreditos(Integer numeroCreditos) {
        this.numeroCreditos = numeroCreditos;
    }

    public List<Materia> getPrerrequisitos() {
        return prerrequisitos;
    }

    public void setPrerrequisitos(List<Materia> prerrequisitos) {
        this.prerrequisitos = prerrequisitos;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
}
