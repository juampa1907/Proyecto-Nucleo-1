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

    /** El identificador único de la matrícula. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MATRICULA")
    private Integer idMatricula;

    /** El estudiante matriculado. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTUDIANTE", nullable = false)
    private Estudiante estudiante;

    /** El periodo de la matrícula. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERIODO", nullable = false)
    private PeriodoAcademico periodo;

    /** La fecha y hora de confirmación, o {@code null} si no se ha confirmado. */
    @Column(name = "FECHA_CONFIRMACION")
    private LocalDateTime fechaConfirmacion;

    /** El código de verificación del último comprobante generado. */
    @Column(name = "CODIGO_VERIFICACION", length = 64)
    private String codigoVerificacion;

    /**
     * Constructor vacío requerido por JPA.
     */
    public MatriculaPeriodo() {
    }

    /**
     * Crea una instancia de {@code MatriculaPeriodo} con los datos indicados.
     *
     * @param estudiante el estudiante matriculado
     * @param periodo el periodo de la matrícula
     */
    public MatriculaPeriodo(Estudiante estudiante, PeriodoAcademico periodo) {
        this.estudiante = estudiante;
        this.periodo = periodo;
    }

    /**
     * Indica si la matrícula ya fue confirmada.
     *
     * @return {@code true} si tiene fecha de confirmación
     */
    public boolean estaConfirmada() {
        return fechaConfirmacion != null;
    }

    /**
     * Obtiene el identificador único de la matrícula.
     *
     * @return el identificador único de la matrícula
     */
    public Integer getIdMatricula() {
        return idMatricula;
    }

    /**
     * Obtiene el estudiante matriculado.
     *
     * @return el estudiante matriculado
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }

    /**
     * Obtiene el periodo de la matrícula.
     *
     * @return el periodo de la matrícula
     */
    public PeriodoAcademico getPeriodo() {
        return periodo;
    }

    /**
     * Obtiene la fecha y hora de confirmación, o {@code null} si no se ha confirmado.
     *
     * @return la fecha y hora de confirmación, o {@code null} si no se ha confirmado
     */
    public LocalDateTime getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    /**
     * Establece la fecha y hora de confirmación, o {@code null} si no se ha confirmado.
     *
     * @param fechaConfirmacion la fecha y hora de confirmación, o {@code null} si no se ha confirmado
     */
    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    /**
     * Obtiene el código de verificación del último comprobante generado.
     *
     * @return el código de verificación del último comprobante generado
     */
    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    /**
     * Establece el código de verificación del último comprobante generado.
     *
     * @param codigoVerificacion el código de verificación del último comprobante generado
     */
    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }
}
