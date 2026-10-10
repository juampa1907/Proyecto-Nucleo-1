package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

/**
 * HU-43: horas presupuestadas por tipo de vinculación en un periodo, para compararlas con la carga asignada.
 */
@Entity
@Table(name = "PRESUPUESTO_VINCULACION",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_PERIODO", "ID_TIPO_VINCULACION"}))
public class PresupuestoVinculacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRESUPUESTO")
    private Integer idPresupuesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODO", nullable = false)
    private PeriodoAcademico periodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_VINCULACION", nullable = false)
    private TipoVinculacion tipoVinculacion;

    @Column(name = "HORAS_PRESUPUESTADAS", nullable = false)
    private Integer horasPresupuestadas;

    public PresupuestoVinculacion() {
    }

    public PresupuestoVinculacion(PeriodoAcademico periodo, TipoVinculacion tipoVinculacion, Integer horasPresupuestadas) {
        this.periodo = periodo;
        this.tipoVinculacion = tipoVinculacion;
        this.horasPresupuestadas = horasPresupuestadas;
    }

    public Integer getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(Integer idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public PeriodoAcademico getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodoAcademico periodo) {
        this.periodo = periodo;
    }

    public TipoVinculacion getTipoVinculacion() {
        return tipoVinculacion;
    }

    public void setTipoVinculacion(TipoVinculacion tipoVinculacion) {
        this.tipoVinculacion = tipoVinculacion;
    }

    public Integer getHorasPresupuestadas() {
        return horasPresupuestadas;
    }

    public void setHorasPresupuestadas(Integer horasPresupuestadas) {
        this.horasPresupuestadas = horasPresupuestadas;
    }
}
