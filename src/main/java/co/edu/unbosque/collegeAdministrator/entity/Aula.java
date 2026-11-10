package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un aula o espacio físico, con su capacidad, tipo, características y bloque. La
 * combinación de código y bloque es única.
 */
@Entity
@Table(name = "AULA", uniqueConstraints = @UniqueConstraint(columnNames = {"CODIGO", "BLOQUE"}))
public class Aula {

    /** El identificador único del aula. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA")
    private Integer idAula;

    /** El código del aula dentro de su bloque. */
    @Column(name = "CODIGO", nullable = false, length = 20)
    private String codigo;

    /** La capacidad de estudiantes del aula. */
    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    /** El tipo de espacio del aula. */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ESPACIO", nullable = false, length = 20)
    private TipoEspacio tipoEspacio;

    /** Las características físicas del aula. */
    @ElementCollection
    @CollectionTable(name = "AULA_CARACTERISTICA", joinColumns = @JoinColumn(name = "ID_AULA"))
    @Enumerated(EnumType.STRING)
    @Column(name = "CARACTERISTICA", nullable = false, length = 30)
    private Set<CaracteristicaAula> caracteristicas = new HashSet<>();

    /** El bloque o edificio donde se encuentra el aula. */
    @Enumerated(EnumType.STRING)
    @Column(name = "BLOQUE", nullable = false, length = 20)
    private Bloque bloque;

    /** Indica si el aula está activa y puede asignarse. */
    @Column(name = "ACTIVA", nullable = false)
    private boolean activa = true;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Aula() {
    }

    /**
     * Crea una instancia de {@code Aula} con los datos indicados.
     *
     * @param codigo el código del aula dentro de su bloque
     * @param capacidad la capacidad de estudiantes del aula
     * @param tipoEspacio el tipo de espacio del aula
     * @param bloque el bloque o edificio donde se encuentra el aula
     */
    public Aula(String codigo, Integer capacidad, TipoEspacio tipoEspacio, Bloque bloque) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.tipoEspacio = tipoEspacio;
        this.bloque = bloque;
    }

    /**
     * Obtiene el identificador único del aula.
     *
     * @return el identificador único del aula
     */
    public Integer getIdAula() {
        return idAula;
    }

    /**
     * Establece el identificador único del aula.
     *
     * @param idAula el identificador único del aula
     */
    public void setIdAula(Integer idAula) {
        this.idAula = idAula;
    }

    /**
     * Obtiene el código del aula dentro de su bloque.
     *
     * @return el código del aula dentro de su bloque
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código del aula dentro de su bloque.
     *
     * @param codigo el código del aula dentro de su bloque
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene la capacidad de estudiantes del aula.
     *
     * @return la capacidad de estudiantes del aula
     */
    public Integer getCapacidad() {
        return capacidad;
    }

    /**
     * Establece la capacidad de estudiantes del aula.
     *
     * @param capacidad la capacidad de estudiantes del aula
     */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    /**
     * Obtiene el tipo de espacio del aula.
     *
     * @return el tipo de espacio del aula
     */
    public TipoEspacio getTipoEspacio() {
        return tipoEspacio;
    }

    /**
     * Establece el tipo de espacio del aula.
     *
     * @param tipoEspacio el tipo de espacio del aula
     */
    public void setTipoEspacio(TipoEspacio tipoEspacio) {
        this.tipoEspacio = tipoEspacio;
    }

    /**
     * Obtiene las características físicas del aula.
     *
     * @return una vista no modificable de las características
     */
    public Set<CaracteristicaAula> getCaracteristicas() {
        return Collections.unmodifiableSet(caracteristicas);
    }

    /**
     * Agrega una característica al aula.
     *
     * @param caracteristica la característica que se agrega
     */
    public void agregarCaracteristica(CaracteristicaAula caracteristica) {
        caracteristicas.add(caracteristica);
    }

    /**
     * Quita una característica del aula.
     *
     * @param caracteristica la característica que se quita
     */
    public void quitarCaracteristica(CaracteristicaAula caracteristica) {
        caracteristicas.remove(caracteristica);
    }

    /**
     * Indica si el aula tiene una característica.
     *
     * @param caracteristica la característica consultada
     * @return {@code true} si el aula la tiene
     */
    public boolean tieneCaracteristica(CaracteristicaAula caracteristica) {
        return caracteristicas.contains(caracteristica);
    }

    /**
     * Obtiene el bloque o edificio donde se encuentra el aula.
     *
     * @return el bloque o edificio donde se encuentra el aula
     */
    public Bloque getBloque() {
        return bloque;
    }

    /**
     * Establece el bloque o edificio donde se encuentra el aula.
     *
     * @param bloque el bloque o edificio donde se encuentra el aula
     */
    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }

    /**
     * Indica si el aula está activa y puede asignarse.
     *
     * @return {@code true} si el aula está activa y puede asignarse
     */
    public boolean isActiva() {
        return activa;
    }

    /**
     * Establece si el aula está activa y puede asignarse.
     *
     * @param activa {@code true} si el aula está activa y puede asignarse
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
