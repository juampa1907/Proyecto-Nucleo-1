package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoHorario;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoPeriodo;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entidad JPA que representa un periodo académico, con sus fechas de inscripción y los estados del periodo y
 * de su horario.
 */
@Entity
@Table(name = "PERIODO_ACADEMICO")
public class PeriodoAcademico {

    /** El identificador único del periodo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERIODO")
    private Integer idPeriodo;

    /** El código único del periodo. */
    @Column(name = "CODIGO", unique = true, nullable = false, length = 20)
    private String codigo;

    /** La fecha de inicio del periodo. */
    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;

    /** La fecha de fin del periodo. */
    @Column(name = "FECHA_FIN", nullable = false)
    private LocalDate fechaFin;

    /** La fecha de apertura de la inscripción. */
    @Column(name = "FECHA_APERTURA_INSCRIPCION", nullable = false)
    private LocalDate fechaAperturaInscripcion;

    /** La fecha de cierre de la inscripción. */
    @Column(name = "FECHA_CIERRE_INSCRIPCION", nullable = false)
    private LocalDate fechaCierreInscripcion;

    /** El estado del periodo. */
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoPeriodo estado = EstadoPeriodo.ABIERTO;

    /** El estado del horario del periodo. */
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_HORARIO", nullable = false, length = 20)
    private EstadoHorario estadoHorario = EstadoHorario.BORRADOR;

    // HU-40: evitan repetir los avisos de apertura y cierre de inscripción.
    /** Indica si ya se envió el aviso de apertura de inscripción (HU-40). */
    @Column(name = "AVISO_APERTURA_ENVIADO", nullable = false)
    private boolean avisoAperturaEnviado;

    /** Indica si ya se envió el aviso de cierre de inscripción (HU-40). */
    @Column(name = "AVISO_CIERRE_ENVIADO", nullable = false)
    private boolean avisoCierreEnviado;

    /**
     * Constructor vacío requerido por JPA.
     */
    public PeriodoAcademico() {
    }

    /**
     * Crea una instancia de {@code PeriodoAcademico} con los datos indicados.
     *
     * @param codigo el código único del periodo
     * @param fechaInicio la fecha de inicio del periodo
     * @param fechaFin la fecha de fin del periodo
     * @param fechaAperturaInscripcion la fecha de apertura de la inscripción
     * @param fechaCierreInscripcion la fecha de cierre de la inscripción
     */
    public PeriodoAcademico(String codigo, LocalDate fechaInicio, LocalDate fechaFin,
                            LocalDate fechaAperturaInscripcion, LocalDate fechaCierreInscripcion) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAperturaInscripcion = fechaAperturaInscripcion;
        this.fechaCierreInscripcion = fechaCierreInscripcion;
    }

    /**
     * Indica si el periodo está cerrado.
     *
     * @return {@code true} si su estado es {@code CERRADO}
     */
    public boolean estaCerrado() {
        return estado == EstadoPeriodo.CERRADO;
    }

    /**
     * Indica si el horario del periodo ya fue publicado.
     *
     * @return {@code true} si su estado de horario es {@code PUBLICADO}
     */
    public boolean horarioPublicado() {
        return estadoHorario == EstadoHorario.PUBLICADO;
    }

    /**
     * Indica si una fecha está dentro de la ventana de inscripción, incluidos sus extremos.
     *
     * @param hoy la fecha evaluada
     * @return {@code true} si la inscripción está abierta en esa fecha
     */
    public boolean inscripcionAbierta(LocalDate hoy) {
        return !hoy.isBefore(fechaAperturaInscripcion) && !hoy.isAfter(fechaCierreInscripcion);
    }

    /**
     * Obtiene el identificador único del periodo.
     *
     * @return el identificador único del periodo
     */
    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    /**
     * Establece el identificador único del periodo.
     *
     * @param idPeriodo el identificador único del periodo
     */
    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    /**
     * Obtiene el código único del periodo.
     *
     * @return el código único del periodo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Establece el código único del periodo.
     *
     * @param codigo el código único del periodo
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene la fecha de inicio del periodo.
     *
     * @return la fecha de inicio del periodo
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio del periodo.
     *
     * @param fechaInicio la fecha de inicio del periodo
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha de fin del periodo.
     *
     * @return la fecha de fin del periodo
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece la fecha de fin del periodo.
     *
     * @param fechaFin la fecha de fin del periodo
     */
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Obtiene la fecha de apertura de la inscripción.
     *
     * @return la fecha de apertura de la inscripción
     */
    public LocalDate getFechaAperturaInscripcion() {
        return fechaAperturaInscripcion;
    }

    /**
     * Establece la fecha de apertura de la inscripción.
     *
     * @param fechaAperturaInscripcion la fecha de apertura de la inscripción
     */
    public void setFechaAperturaInscripcion(LocalDate fechaAperturaInscripcion) {
        this.fechaAperturaInscripcion = fechaAperturaInscripcion;
    }

    /**
     * Obtiene la fecha de cierre de la inscripción.
     *
     * @return la fecha de cierre de la inscripción
     */
    public LocalDate getFechaCierreInscripcion() {
        return fechaCierreInscripcion;
    }

    /**
     * Establece la fecha de cierre de la inscripción.
     *
     * @param fechaCierreInscripcion la fecha de cierre de la inscripción
     */
    public void setFechaCierreInscripcion(LocalDate fechaCierreInscripcion) {
        this.fechaCierreInscripcion = fechaCierreInscripcion;
    }

    /**
     * Obtiene el estado del periodo.
     *
     * @return el estado del periodo
     */
    public EstadoPeriodo getEstado() {
        return estado;
    }

    /**
     * Establece el estado del periodo.
     *
     * @param estado el estado del periodo
     */
    public void setEstado(EstadoPeriodo estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el estado del horario del periodo.
     *
     * @return el estado del horario del periodo
     */
    public EstadoHorario getEstadoHorario() {
        return estadoHorario;
    }

    /**
     * Establece el estado del horario del periodo.
     *
     * @param estadoHorario el estado del horario del periodo
     */
    public void setEstadoHorario(EstadoHorario estadoHorario) {
        this.estadoHorario = estadoHorario;
    }

    /**
     * Indica si ya se envió el aviso de apertura de inscripción (HU-40).
     *
     * @return {@code true} si ya se envió el aviso de apertura de inscripción (HU-40)
     */
    public boolean isAvisoAperturaEnviado() {
        return avisoAperturaEnviado;
    }

    /**
     * Establece si ya se envió el aviso de apertura de inscripción (HU-40).
     *
     * @param avisoAperturaEnviado {@code true} si ya se envió el aviso de apertura de inscripción (HU-40)
     */
    public void setAvisoAperturaEnviado(boolean avisoAperturaEnviado) {
        this.avisoAperturaEnviado = avisoAperturaEnviado;
    }

    /**
     * Indica si ya se envió el aviso de cierre de inscripción (HU-40).
     *
     * @return {@code true} si ya se envió el aviso de cierre de inscripción (HU-40)
     */
    public boolean isAvisoCierreEnviado() {
        return avisoCierreEnviado;
    }

    /**
     * Establece si ya se envió el aviso de cierre de inscripción (HU-40).
     *
     * @param avisoCierreEnviado {@code true} si ya se envió el aviso de cierre de inscripción (HU-40)
     */
    public void setAvisoCierreEnviado(boolean avisoCierreEnviado) {
        this.avisoCierreEnviado = avisoCierreEnviado;
    }
}
