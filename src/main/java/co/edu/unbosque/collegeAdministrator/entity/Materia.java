package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
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

    @ManyToMany
    @JoinTable(
            name = "MATERIA_PRERREQUISITO",
            joinColumns = @JoinColumn(name = "ID_MATERIA"),
            inverseJoinColumns = @JoinColumn(name = "ID_PRERREQUISITO")
    )
    private List<Materia> prerrequisitos = new ArrayList<>();

    @OneToMany(mappedBy = "materia")
    private List<Grupo> grupos = new ArrayList<>();

    public Materia() {
    }

    public Materia(String nombre, Integer numeroCreditos) {
        this.nombre = nombre;
        this.numeroCreditos = numeroCreditos;
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
        return Collections.unmodifiableList(prerrequisitos);
    }

    public void agregarPrerrequisito(Materia prerrequisito) {
        prerrequisitos.add(prerrequisito);
    }

    public void quitarPrerrequisito(Materia prerrequisito) {
        prerrequisitos.remove(prerrequisito);
    }

    public List<Grupo> getGrupos() {
        return Collections.unmodifiableList(grupos);
    }
}
