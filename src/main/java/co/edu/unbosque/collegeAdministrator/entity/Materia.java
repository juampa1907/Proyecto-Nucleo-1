package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad JPA que representa una materia, con sus créditos, horas, requisitos de aula, prerrequisitos y
 * correquisitos.
 */
@Entity
@Table(name = "MATERIA")
public class Materia {

    /** El identificador único de la materia. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATERIA")
    private Integer idMateria;

    /** El código único de la materia. */
    @Column(name = "CODIGO", unique = true, nullable = false, length = 20)
    private String codigo;

    /** El nombre de la materia. */
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    /** El número de créditos de la materia. */
    @Column(name = "CREDITOS", nullable = false)
    private Integer numeroCreditos;

    /** Las horas semanales de la materia. */
    @Column(name = "HORAS_SEMANALES", nullable = false)
    private Integer horasSemanales;

    /** Las características que debe tener el aula de la materia. */
    @ElementCollection
    @CollectionTable(name = "MATERIA_REQUISITO_AULA", joinColumns = @JoinColumn(name = "ID_MATERIA"))
    @Enumerated(EnumType.STRING)
    @Column(name = "CARACTERISTICA", nullable = false, length = 30)
    private Set<CaracteristicaAula> requisitosAula = new HashSet<>();

    /** Las materias que deben estar aprobadas antes de inscribir esta. */
    @ManyToMany
    @JoinTable(
            name = "MATERIA_PRERREQUISITO",
            joinColumns = @JoinColumn(name = "ID_MATERIA"),
            inverseJoinColumns = @JoinColumn(name = "ID_PRERREQUISITO")
    )
    private Set<Materia> prerrequisitos = new HashSet<>();

    /** Las materias que deben estar aprobadas o cursándose al inscribir esta. */
    @ManyToMany
    @JoinTable(
            name = "MATERIA_CORREQUISITO",
            joinColumns = @JoinColumn(name = "ID_MATERIA"),
            inverseJoinColumns = @JoinColumn(name = "ID_CORREQUISITO")
    )
    private Set<Materia> correquisitos = new HashSet<>();

    /** Los grupos abiertos para la materia. */
    @OneToMany(mappedBy = "materia")
    private List<Grupo> grupos = new ArrayList<>();

    /**
     * Constructor vacío requerido por JPA.
     */
    public Materia() {
    }

    /**
     * Crea una instancia de {@code Materia} con los datos indicados.
     *
     * @param codigo el código único de la materia
     * @param nombre el nombre de la materia
     * @param numeroCreditos el número de créditos de la materia
     * @param horasSemanales las horas semanales de la materia
     */
    public Materia(String codigo, String nombre, Integer numeroCreditos, Integer horasSemanales) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.numeroCreditos = numeroCreditos;
        this.horasSemanales = horasSemanales;
    }

    /**
     * Obtiene el identificador único de la materia.
     *
     * @return el identificador único de la materia
     */
    public Integer getIdMateria() {
        return idMateria;
    }

    /**
     * Establece el identificador único de la materia.
     *
     * @param idMateria el identificador único de la materia
     */
    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    /**
     * Obtiene el código único de la materia.
     *
     * @return el código único de la materia
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código único de la materia.
     *
     * @param codigo el código único de la materia
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene el nombre de la materia.
     *
     * @return el nombre de la materia
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la materia.
     *
     * @param nombre el nombre de la materia
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el número de créditos de la materia.
     *
     * @return el número de créditos de la materia
     */
    public Integer getNumeroCreditos() {
        return numeroCreditos;
    }

    /**
     * Establece el número de créditos de la materia.
     *
     * @param numeroCreditos el número de créditos de la materia
     */
    public void setNumeroCreditos(Integer numeroCreditos) {
        this.numeroCreditos = numeroCreditos;
    }

    /**
     * Obtiene las horas semanales de la materia.
     *
     * @return las horas semanales de la materia
     */
    public Integer getHorasSemanales() {
        return horasSemanales;
    }

    /**
     * Establece las horas semanales de la materia.
     *
     * @param horasSemanales las horas semanales de la materia
     */
    public void setHorasSemanales(Integer horasSemanales) {
        this.horasSemanales = horasSemanales;
    }

    /**
     * Obtiene los requisitos de aula de la materia.
     *
     * @return una vista no modificable de los requisitos
     */
    public Set<CaracteristicaAula> getRequisitosAula() {
        return Collections.unmodifiableSet(requisitosAula);
    }

    /**
     * Agrega un requisito de aula.
     *
     * @param requisito la característica exigida
     */
    public void agregarRequisitoAula(CaracteristicaAula requisito) {
        requisitosAula.add(requisito);
    }

    /**
     * Quita un requisito de aula.
     *
     * @param requisito la característica que deja de exigirse
     */
    public void quitarRequisitoAula(CaracteristicaAula requisito) {
        requisitosAula.remove(requisito);
    }

    /**
     * Obtiene los prerrequisitos de la materia.
     *
     * @return una vista no modificable de los prerrequisitos
     */
    public Set<Materia> getPrerrequisitos() {
        return Collections.unmodifiableSet(prerrequisitos);
    }

    /**
     * Agrega un prerrequisito.
     *
     * @param prerrequisito la materia prerrequisito
     */
    public void agregarPrerrequisito(Materia prerrequisito) {
        prerrequisitos.add(prerrequisito);
    }

    /**
     * Quita un prerrequisito.
     *
     * @param prerrequisito la materia que deja de ser prerrequisito
     */
    public void quitarPrerrequisito(Materia prerrequisito) {
        prerrequisitos.remove(prerrequisito);
    }

    /**
     * Obtiene los correquisitos de la materia.
     *
     * @return una vista no modificable de los correquisitos
     */
    public Set<Materia> getCorrequisitos() {
        return Collections.unmodifiableSet(correquisitos);
    }

    /**
     * Agrega un correquisito.
     *
     * @param correquisito la materia correquisito
     */
    public void agregarCorrequisito(Materia correquisito) {
        correquisitos.add(correquisito);
    }

    /**
     * Quita un correquisito.
     *
     * @param correquisito la materia que deja de ser correquisito
     */
    public void quitarCorrequisito(Materia correquisito) {
        correquisitos.remove(correquisito);
    }

    /**
     * Obtiene los grupos de la materia.
     *
     * @return una vista no modificable de los grupos
     */
    public List<Grupo> getGrupos() {
        return Collections.unmodifiableList(grupos);
    }
}
