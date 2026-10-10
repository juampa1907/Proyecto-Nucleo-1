package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * HU-38: confirmación de la matrícula de un estudiante en un periodo (valida créditos mínimos y máximos).
 * HU-37: guarda el código de verificación del último comprobante generado.
 */
@Entity
@Table(name = "MATRICULA_PERIODO", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_ESTUDIANTE", "ID_PERIODO"}))
public class MatriculaPeriodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATRICULA")
    private Integer idMatricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTUDIANTE", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODO", nullable = false)
    private PeriodoAcademico periodo;

    @Column(name = "FECHA_CONFIRMACION")
    private LocalDateTime fechaConfirmacion;

    @Column(name = "CODIGO_VERIFICACION", length = 64)
    private String codigoVerificacion;

    public MatriculaPeriodo() {
    }

    public MatriculaPeriodo(Estudiante estudiante, PeriodoAcademico periodo) {
        this.estudiante = estudiante;
        this.periodo = periodo;
    }

    public boolean estaConfirmada() {
        return fechaConfirmacion != null;
    }

    public Integer getIdMatricula() {
        return idMatricula;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public PeriodoAcademico getPeriodo() {
        return periodo;
    }

    public LocalDateTime getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }
}
