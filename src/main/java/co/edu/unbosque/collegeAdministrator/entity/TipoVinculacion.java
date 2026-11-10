package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa un tipo de vinculación docente y su carga horaria máxima.
 */
@Entity
@Table(name = "TIPO_VINCULACION")
public class TipoVinculacion {

    /** El identificador único del tipo de vinculación. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TIPO_VINCULACION")
    private Integer idTipoVinculacion;

    /** El nombre único del tipo de vinculación. */
    @Column(name = "NOMBRE", unique = true, nullable = false, length = 50)
    private String nombre;

    /** Las horas máximas semanales permitidas. */
    @Column(name = "HORAS_MAXIMAS_SEMANALES", nullable = false)
    private Integer horasMaximasSemanales;

    /**
     * Constructor vacío requerido por JPA.
     */
    public TipoVinculacion() {
    }

    /**
     * Crea una instancia de {@code TipoVinculacion} con los datos indicados.
     *
     * @param nombre el nombre único del tipo de vinculación
     * @param horasMaximasSemanales las horas máximas semanales permitidas
     */
    public TipoVinculacion(String nombre, Integer horasMaximasSemanales) {
        this.nombre = nombre;
        this.horasMaximasSemanales = horasMaximasSemanales;
    }

    /**
     * Obtiene el identificador único del tipo de vinculación.
     *
     * @return el identificador único del tipo de vinculación
     */
    public Integer getIdTipoVinculacion() {
        return idTipoVinculacion;
    }

    /**
     * Establece el identificador único del tipo de vinculación.
     *
     * @param idTipoVinculacion el identificador único del tipo de vinculación
     */
    public void setIdTipoVinculacion(Integer idTipoVinculacion) {
        this.idTipoVinculacion = idTipoVinculacion;
    }

    /**
     * Obtiene el nombre único del tipo de vinculación.
     *
     * @return el nombre único del tipo de vinculación
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre único del tipo de vinculación.
     *
     * @param nombre el nombre único del tipo de vinculación
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene las horas máximas semanales permitidas.
     *
     * @return las horas máximas semanales permitidas
     */
    public Integer getHorasMaximasSemanales() {
        return horasMaximasSemanales;
    }

    /**
     * Establece las horas máximas semanales permitidas.
     *
     * @param horasMaximasSemanales las horas máximas semanales permitidas
     */
    public void setHorasMaximasSemanales(Integer horasMaximasSemanales) {
        this.horasMaximasSemanales = horasMaximasSemanales;
    }
}
