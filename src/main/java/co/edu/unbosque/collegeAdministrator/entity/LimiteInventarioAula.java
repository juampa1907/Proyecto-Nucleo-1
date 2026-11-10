package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import jakarta.persistence.*;

/**
 * ESP-F-02 y ESP-F-04: tope de aulas por tipo de espacio y sede, según el inventario de la Unidad de Espacios Físicos.
 */
@Entity
@Table(name = "LIMITE_INVENTARIO_AULA", uniqueConstraints = @UniqueConstraint(columnNames = {"TIPO_ESPACIO", "SEDE"}))
public class LimiteInventarioAula {

    /** El identificador único del tope. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LIMITE")
    private Integer idLimite;

    /** El tipo de espacio al que aplica el tope. */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ESPACIO", nullable = false, length = 20)
    private TipoEspacio tipoEspacio;

    /** La sede a la que aplica el tope. */
    @Enumerated(EnumType.STRING)
    @Column(name = "SEDE", nullable = false, length = 20)
    private Sede sede;

    /** El número máximo de aulas permitidas. */
    @Column(name = "MAXIMO", nullable = false)
    private Integer maximo;

    /**
     * Constructor vacío requerido por JPA.
     */
    public LimiteInventarioAula() {
    }

    /**
     * Crea una instancia de {@code LimiteInventarioAula} con los datos indicados.
     *
     * @param tipoEspacio el tipo de espacio al que aplica el tope
     * @param sede la sede a la que aplica el tope
     * @param maximo el número máximo de aulas permitidas
     */
    public LimiteInventarioAula(TipoEspacio tipoEspacio, Sede sede, Integer maximo) {
        this.tipoEspacio = tipoEspacio;
        this.sede = sede;
        this.maximo = maximo;
    }

    /**
     * Obtiene el identificador único del tope.
     *
     * @return el identificador único del tope
     */
    public Integer getIdLimite() {
        return idLimite;
    }

    /**
     * Establece el identificador único del tope.
     *
     * @param idLimite el identificador único del tope
     */
    public void setIdLimite(Integer idLimite) {
        this.idLimite = idLimite;
    }

    /**
     * Obtiene el tipo de espacio al que aplica el tope.
     *
     * @return el tipo de espacio al que aplica el tope
     */
    public TipoEspacio getTipoEspacio() {
        return tipoEspacio;
    }

    /**
     * Establece el tipo de espacio al que aplica el tope.
     *
     * @param tipoEspacio el tipo de espacio al que aplica el tope
     */
    public void setTipoEspacio(TipoEspacio tipoEspacio) {
        this.tipoEspacio = tipoEspacio;
    }

    /**
     * Obtiene la sede a la que aplica el tope.
     *
     * @return la sede a la que aplica el tope
     */
    public Sede getSede() {
        return sede;
    }

    /**
     * Establece la sede a la que aplica el tope.
     *
     * @param sede la sede a la que aplica el tope
     */
    public void setSede(Sede sede) {
        this.sede = sede;
    }

    /**
     * Obtiene el número máximo de aulas permitidas.
     *
     * @return el número máximo de aulas permitidas
     */
    public Integer getMaximo() {
        return maximo;
    }

    /**
     * Establece el número máximo de aulas permitidas.
     *
     * @param maximo el número máximo de aulas permitidas
     */
    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }
}
