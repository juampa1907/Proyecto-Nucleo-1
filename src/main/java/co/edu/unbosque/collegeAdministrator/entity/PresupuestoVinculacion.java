package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

/**
 * HU-43: horas presupuestadas por tipo de vinculación en un periodo, para compararlas con la carga asignada.
 */
@Entity
@Table(name = "PRESUPUESTO_VINCULACION",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_PERIODO", "ID_TIPO_VINCULACION"}))
public class PresupuestoVinculacion {

    /** El identificador único del presupuesto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRESUPUESTO")
    private Integer idPresupuesto;

    /** El periodo del presupuesto. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODO", nullable = false)
    private PeriodoAcademico periodo;

    /** El tipo de vinculación presupuestado. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_VINCULACION", nullable = false)
    private TipoVinculacion tipoVinculacion;

    /** Las horas presupuestadas. */
    @Column(name = "HORAS_PRESUPUESTADAS", nullable = false)
    private Integer horasPresupuestadas;

    /**
     * Constructor vacío requerido por JPA.
     */
    public PresupuestoVinculacion() {
    }

    /**
     * Crea una instancia de {@code PresupuestoVinculacion} con los datos indicados.
     *
     * @param periodo el periodo del presupuesto
     * @param tipoVinculacion el tipo de vinculación presupuestado
     * @param horasPresupuestadas las horas presupuestadas
     */
    public PresupuestoVinculacion(PeriodoAcademico periodo, TipoVinculacion tipoVinculacion, Integer horasPresupuestadas) {
        this.periodo = periodo;
        this.tipoVinculacion = tipoVinculacion;
        this.horasPresupuestadas = horasPresupuestadas;
    }

    /**
     * Obtiene el identificador único del presupuesto.
     *
     * @return el identificador único del presupuesto
     */
    public Integer getIdPresupuesto() {
        return idPresupuesto;
    }

    /**
     * Establece el identificador único del presupuesto.
     *
     * @param idPresupuesto el identificador único del presupuesto
     */
    public void setIdPresupuesto(Integer idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    /**
     * Obtiene el periodo del presupuesto.
     *
     * @return el periodo del presupuesto
     */
    public PeriodoAcademico getPeriodo() {
        return periodo;
    }

    /**
     * Establece el periodo del presupuesto.
     *
     * @param periodo el periodo del presupuesto
     */
    public void setPeriodo(PeriodoAcademico periodo) {
        this.periodo = periodo;
    }

    /**
     * Obtiene el tipo de vinculación presupuestado.
     *
     * @return el tipo de vinculación presupuestado
     */
    public TipoVinculacion getTipoVinculacion() {
        return tipoVinculacion;
    }

    /**
     * Establece el tipo de vinculación presupuestado.
     *
     * @param tipoVinculacion el tipo de vinculación presupuestado
     */
    public void setTipoVinculacion(TipoVinculacion tipoVinculacion) {
        this.tipoVinculacion = tipoVinculacion;
    }

    /**
     * Obtiene las horas presupuestadas.
     *
     * @return las horas presupuestadas
     */
    public Integer getHorasPresupuestadas() {
        return horasPresupuestadas;
    }

    /**
     * Establece las horas presupuestadas.
     *
     * @param horasPresupuestadas las horas presupuestadas
     */
    public void setHorasPresupuestadas(Integer horasPresupuestadas) {
        this.horasPresupuestadas = horasPresupuestadas;
    }
}
