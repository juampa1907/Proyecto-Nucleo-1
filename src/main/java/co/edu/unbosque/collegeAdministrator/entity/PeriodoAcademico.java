package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoHorario;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoPeriodo;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "PERIODO_ACADEMICO")
public class PeriodoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERIODO")
    private Integer idPeriodo;

    @Column(name = "CODIGO", unique = true, nullable = false, length = 20)
    private String codigo;

    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "FECHA_FIN", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "FECHA_APERTURA_INSCRIPCION", nullable = false)
    private LocalDate fechaAperturaInscripcion;

    @Column(name = "FECHA_CIERRE_INSCRIPCION", nullable = false)
    private LocalDate fechaCierreInscripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoPeriodo estado = EstadoPeriodo.ABIERTO;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_HORARIO", nullable = false, length = 20)
    private EstadoHorario estadoHorario = EstadoHorario.BORRADOR;

    // HU-40: evitan repetir los avisos de apertura y cierre de inscripción.
    @Column(name = "AVISO_APERTURA_ENVIADO", nullable = false)
    private boolean avisoAperturaEnviado;

    @Column(name = "AVISO_CIERRE_ENVIADO", nullable = false)
    private boolean avisoCierreEnviado;

    public PeriodoAcademico() {
    }

    public PeriodoAcademico(String codigo, LocalDate fechaInicio, LocalDate fechaFin,
                            LocalDate fechaAperturaInscripcion, LocalDate fechaCierreInscripcion) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaAperturaInscripcion = fechaAperturaInscripcion;
        this.fechaCierreInscripcion = fechaCierreInscripcion;
    }

    public boolean estaCerrado() {
        return estado == EstadoPeriodo.CERRADO;
    }

    public boolean horarioPublicado() {
        return estadoHorario == EstadoHorario.PUBLICADO;
    }

    public boolean inscripcionAbierta(LocalDate hoy) {
        return !hoy.isBefore(fechaAperturaInscripcion) && !hoy.isAfter(fechaCierreInscripcion);
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaAperturaInscripcion() {
        return fechaAperturaInscripcion;
    }

    public void setFechaAperturaInscripcion(LocalDate fechaAperturaInscripcion) {
        this.fechaAperturaInscripcion = fechaAperturaInscripcion;
    }

    public LocalDate getFechaCierreInscripcion() {
        return fechaCierreInscripcion;
    }

    public void setFechaCierreInscripcion(LocalDate fechaCierreInscripcion) {
        this.fechaCierreInscripcion = fechaCierreInscripcion;
    }

    public EstadoPeriodo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPeriodo estado) {
        this.estado = estado;
    }

    public EstadoHorario getEstadoHorario() {
        return estadoHorario;
    }

    public void setEstadoHorario(EstadoHorario estadoHorario) {
        this.estadoHorario = estadoHorario;
    }

    public boolean isAvisoAperturaEnviado() {
        return avisoAperturaEnviado;
    }

    public void setAvisoAperturaEnviado(boolean avisoAperturaEnviado) {
        this.avisoAperturaEnviado = avisoAperturaEnviado;
    }

    public boolean isAvisoCierreEnviado() {
        return avisoCierreEnviado;
    }

    public void setAvisoCierreEnviado(boolean avisoCierreEnviado) {
        this.avisoCierreEnviado = avisoCierreEnviado;
    }
}
