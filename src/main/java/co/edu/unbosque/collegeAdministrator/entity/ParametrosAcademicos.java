package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import jakarta.persistence.*;

import java.time.LocalTime;

/**
 * Parámetros institucionales editables sin tocar código (RF-31, RF-50 a RF-55, ESP-F-01).
 * Se guarda una sola fila. Los valores por defecto son los del documento de requisitos;
 * los que el documento no define quedan en null y su regla no se aplica hasta configurarlos.
 */
@Entity
@Table(name = "PARAMETROS_ACADEMICOS")
public class ParametrosAcademicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PARAMETROS")
    private Integer idParametros;

    @Column(name = "TAMANO_MAXIMO_GRUPO", nullable = false)
    private Integer tamanoMaximoGrupo = 40;

    @Column(name = "TOLERANCIA_CUPO_PORCENTAJE", nullable = false)
    private Integer toleranciaCupoPorcentaje = 10;

    @Column(name = "MINIMO_ESTUDIANTES_GRUPO")
    private Integer minimoEstudiantesGrupo;

    @Column(name = "HORA_INICIO_SEMANA", nullable = false)
    private LocalTime horaInicioSemana = LocalTime.of(7, 0);

    @Column(name = "HORA_FIN_SEMANA", nullable = false)
    private LocalTime horaFinSemana = LocalTime.of(22, 0);

    @Column(name = "HORA_INICIO_SABADO", nullable = false)
    private LocalTime horaInicioSabado = LocalTime.of(7, 0);

    @Column(name = "HORA_FIN_SABADO", nullable = false)
    private LocalTime horaFinSabado = LocalTime.of(13, 0);

    @Column(name = "INICIO_ALMUERZO", nullable = false)
    private LocalTime inicioAlmuerzo = LocalTime.of(12, 0);

    @Column(name = "FIN_ALMUERZO", nullable = false)
    private LocalTime finAlmuerzo = LocalTime.of(13, 0);

    @Column(name = "DURACION_SESION_MINUTOS", nullable = false)
    private Integer duracionSesionMinutos = 120;

    @Column(name = "MAX_SESIONES_SEMANALES", nullable = false)
    private Integer maxSesionesSemanales = 4;

    @Column(name = "CREDITOS_MINIMOS")
    private Integer creditosMinimos;

    @Column(name = "CREDITOS_MAXIMOS")
    private Integer creditosMaximos;

    // HU-40: días de anticipación de los avisos de apertura y cierre de inscripción.
    @Column(name = "DIAS_ANTICIPACION_AVISO", nullable = false)
    private Integer diasAnticipacionAviso = 3;

    // HU-08: por debajo de este porcentaje de su máximo, un docente se considera subutilizado.
    // El documento no da el valor; 75 es un valor inicial editable.
    @Column(name = "UMBRAL_SUBUTILIZACION_PORCENTAJE", nullable = false)
    private Integer umbralSubutilizacionPorcentaje = 75;

    public ParametrosAcademicos() {
    }

    public int calcularCupoMaximoPermitido() {
        return tamanoMaximoGrupo * (100 + toleranciaCupoPorcentaje) / 100;
    }

    // RF-23 y RF-24: dentro de la franja institucional del día y sin tocar el almuerzo.
    public boolean franjaPermitida(FranjaHoraria franja) {
        boolean sabado = franja.getDiaSemana() == DiaSemana.SABADO;
        LocalTime inicioDia = sabado ? horaInicioSabado : horaInicioSemana;
        LocalTime finDia = sabado ? horaFinSabado : horaFinSemana;
        boolean dentroDelDia = !franja.getHoraInicio().isBefore(inicioDia) && !franja.getHoraFin().isAfter(finDia);
        boolean tocaAlmuerzo = franja.getHoraInicio().isBefore(finAlmuerzo) && inicioAlmuerzo.isBefore(franja.getHoraFin());
        return dentroDelDia && !tocaAlmuerzo;
    }

    public Integer getIdParametros() {
        return idParametros;
    }

    public void setIdParametros(Integer idParametros) {
        this.idParametros = idParametros;
    }

    public Integer getTamanoMaximoGrupo() {
        return tamanoMaximoGrupo;
    }

    public void setTamanoMaximoGrupo(Integer tamanoMaximoGrupo) {
        this.tamanoMaximoGrupo = tamanoMaximoGrupo;
    }

    public Integer getToleranciaCupoPorcentaje() {
        return toleranciaCupoPorcentaje;
    }

    public void setToleranciaCupoPorcentaje(Integer toleranciaCupoPorcentaje) {
        this.toleranciaCupoPorcentaje = toleranciaCupoPorcentaje;
    }

    public Integer getMinimoEstudiantesGrupo() {
        return minimoEstudiantesGrupo;
    }

    public void setMinimoEstudiantesGrupo(Integer minimoEstudiantesGrupo) {
        this.minimoEstudiantesGrupo = minimoEstudiantesGrupo;
    }

    public LocalTime getHoraInicioSemana() {
        return horaInicioSemana;
    }

    public void setHoraInicioSemana(LocalTime horaInicioSemana) {
        this.horaInicioSemana = horaInicioSemana;
    }

    public LocalTime getHoraFinSemana() {
        return horaFinSemana;
    }

    public void setHoraFinSemana(LocalTime horaFinSemana) {
        this.horaFinSemana = horaFinSemana;
    }

    public LocalTime getHoraInicioSabado() {
        return horaInicioSabado;
    }

    public void setHoraInicioSabado(LocalTime horaInicioSabado) {
        this.horaInicioSabado = horaInicioSabado;
    }

    public LocalTime getHoraFinSabado() {
        return horaFinSabado;
    }

    public void setHoraFinSabado(LocalTime horaFinSabado) {
        this.horaFinSabado = horaFinSabado;
    }

    public LocalTime getInicioAlmuerzo() {
        return inicioAlmuerzo;
    }

    public void setInicioAlmuerzo(LocalTime inicioAlmuerzo) {
        this.inicioAlmuerzo = inicioAlmuerzo;
    }

    public LocalTime getFinAlmuerzo() {
        return finAlmuerzo;
    }

    public void setFinAlmuerzo(LocalTime finAlmuerzo) {
        this.finAlmuerzo = finAlmuerzo;
    }

    public Integer getDuracionSesionMinutos() {
        return duracionSesionMinutos;
    }

    public void setDuracionSesionMinutos(Integer duracionSesionMinutos) {
        this.duracionSesionMinutos = duracionSesionMinutos;
    }

    public Integer getMaxSesionesSemanales() {
        return maxSesionesSemanales;
    }

    public void setMaxSesionesSemanales(Integer maxSesionesSemanales) {
        this.maxSesionesSemanales = maxSesionesSemanales;
    }

    public Integer getCreditosMinimos() {
        return creditosMinimos;
    }

    public void setCreditosMinimos(Integer creditosMinimos) {
        this.creditosMinimos = creditosMinimos;
    }

    public Integer getCreditosMaximos() {
        return creditosMaximos;
    }

    public void setCreditosMaximos(Integer creditosMaximos) {
        this.creditosMaximos = creditosMaximos;
    }

    public Integer getDiasAnticipacionAviso() {
        return diasAnticipacionAviso;
    }

    public void setDiasAnticipacionAviso(Integer diasAnticipacionAviso) {
        this.diasAnticipacionAviso = diasAnticipacionAviso;
    }

    public Integer getUmbralSubutilizacionPorcentaje() {
        return umbralSubutilizacionPorcentaje;
    }

    public void setUmbralSubutilizacionPorcentaje(Integer umbralSubutilizacionPorcentaje) {
        this.umbralSubutilizacionPorcentaje = umbralSubutilizacionPorcentaje;
    }
}
