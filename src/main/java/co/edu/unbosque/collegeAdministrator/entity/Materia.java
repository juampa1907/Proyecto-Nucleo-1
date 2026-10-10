package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MATERIA")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIA")
    private Integer idMateria;

    @Column(name = "CODIGO", unique = true, nullable = false, length = 20)
    private String codigo;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "CREDITOS", nullable = false)
    private Integer numeroCreditos;

    @Column(name = "HORAS_SEMANALES", nullable = false)
    private Integer horasSemanales;

    @ElementCollection
    @CollectionTable(name = "MATERIA_REQUISITO_AULA", joinColumns = @JoinColumn(name = "ID_MATERIA"))
    @Enumerated(EnumType.STRING)
    @Column(name = "CARACTERISTICA", nullable = false, length = 30)
    private Set<CaracteristicaAula> requisitosAula = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "MATERIA_PRERREQUISITO",
            joinColumns = @JoinColumn(name = "ID_MATERIA"),
            inverseJoinColumns = @JoinColumn(name = "ID_PRERREQUISITO")
    )
    private Set<Materia> prerrequisitos = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "MATERIA_CORREQUISITO",
            joinColumns = @JoinColumn(name = "ID_MATERIA"),
            inverseJoinColumns = @JoinColumn(name = "ID_CORREQUISITO")
    )
    private Set<Materia> correquisitos = new HashSet<>();

    @OneToMany(mappedBy = "materia")
    private List<Grupo> grupos = new ArrayList<>();

    public Materia() {
    }

    public Materia(String codigo, String nombre, Integer numeroCreditos, Integer horasSemanales) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.numeroCreditos = numeroCreditos;
        this.horasSemanales = horasSemanales;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public Integer getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(Integer horasSemanales) {
        this.horasSemanales = horasSemanales;
    }

    public Set<CaracteristicaAula> getRequisitosAula() {
        return Collections.unmodifiableSet(requisitosAula);
    }

    public void agregarRequisitoAula(CaracteristicaAula requisito) {
        requisitosAula.add(requisito);
    }

    public void quitarRequisitoAula(CaracteristicaAula requisito) {
        requisitosAula.remove(requisito);
    }

    public Set<Materia> getPrerrequisitos() {
        return Collections.unmodifiableSet(prerrequisitos);
    }

    public void agregarPrerrequisito(Materia prerrequisito) {
        prerrequisitos.add(prerrequisito);
    }

    public void quitarPrerrequisito(Materia prerrequisito) {
        prerrequisitos.remove(prerrequisito);
    }

    public Set<Materia> getCorrequisitos() {
        return Collections.unmodifiableSet(correquisitos);
    }

    public void agregarCorrequisito(Materia correquisito) {
        correquisitos.add(correquisito);
    }

    public void quitarCorrequisito(Materia correquisito) {
        correquisitos.remove(correquisito);
    }

    public List<Grupo> getGrupos() {
        return Collections.unmodifiableList(grupos);
    }
}
