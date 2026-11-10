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

    /** El identificador de la fila de parámetros. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PARAMETROS")
    private Integer idParametros;

    /** El tamaño máximo de un grupo. */
    @Column(name = "TAMANO_MAXIMO_GRUPO", nullable = false)
    private Integer tamanoMaximoGrupo = 40;

    /** El porcentaje de tolerancia sobre el tamaño máximo de grupo. */
    @Column(name = "TOLERANCIA_CUPO_PORCENTAJE", nullable = false)
    private Integer toleranciaCupoPorcentaje = 10;

    /** El mínimo de estudiantes por grupo, o {@code null} si no está configurado. */
    @Column(name = "MINIMO_ESTUDIANTES_GRUPO")
    private Integer minimoEstudiantesGrupo;

    /** La hora de inicio de la jornada de lunes a viernes. */
    @Column(name = "HORA_INICIO_SEMANA", nullable = false)
    private LocalTime horaInicioSemana = LocalTime.of(7, 0);

    /** La hora de fin de la jornada de lunes a viernes. */
    @Column(name = "HORA_FIN_SEMANA", nullable = false)
    private LocalTime horaFinSemana = LocalTime.of(22, 0);

    /** La hora de inicio de la jornada del sábado. */
    @Column(name = "HORA_INICIO_SABADO", nullable = false)
    private LocalTime horaInicioSabado = LocalTime.of(7, 0);

    /** La hora de fin de la jornada del sábado. */
    @Column(name = "HORA_FIN_SABADO", nullable = false)
    private LocalTime horaFinSabado = LocalTime.of(13, 0);

    /** La hora de inicio de la franja de almuerzo. */
    @Column(name = "INICIO_ALMUERZO", nullable = false)
    private LocalTime inicioAlmuerzo = LocalTime.of(12, 0);

    /** La hora de fin de la franja de almuerzo. */
    @Column(name = "FIN_ALMUERZO", nullable = false)
    private LocalTime finAlmuerzo = LocalTime.of(13, 0);

    /** La duración obligatoria de cada sesión en minutos. */
    @Column(name = "DURACION_SESION_MINUTOS", nullable = false)
    private Integer duracionSesionMinutos = 120;

    /** El máximo de sesiones semanales por grupo. */
    @Column(name = "MAX_SESIONES_SEMANALES", nullable = false)
    private Integer maxSesionesSemanales = 4;

    /** Los créditos mínimos para matricular, o {@code null} si no aplica. */
    @Column(name = "CREDITOS_MINIMOS")
    private Integer creditosMinimos;

    /** Los créditos máximos para matricular, o {@code null} si no aplica. */
    @Column(name = "CREDITOS_MAXIMOS")
    private Integer creditosMaximos;

    // HU-40: días de anticipación de los avisos de apertura y cierre de inscripción.
    /** Los días de anticipación de los avisos de apertura y cierre de inscripción (HU-40). */
    @Column(name = "DIAS_ANTICIPACION_AVISO", nullable = false)
    private Integer diasAnticipacionAviso = 3;

    // HU-08: por debajo de este porcentaje de su máximo, un docente se considera subutilizado.
    // El documento no da el valor; 75 es un valor inicial editable.
    /** El porcentaje de la carga máxima bajo el cual un docente se considera subutilizado (HU-08). */
    @Column(name = "UMBRAL_SUBUTILIZACION_PORCENTAJE", nullable = false)
    private Integer umbralSubutilizacionPorcentaje = 75;

    /**
     * Crea los parámetros con sus valores por defecto.
     */
    public ParametrosAcademicos() {
    }

    /**
     * Calcula el cupo máximo permitido, sumando la tolerancia al tamaño máximo de grupo.
     *
     * @return el cupo máximo permitido
     */
    public int calcularCupoMaximoPermitido() {
        return tamanoMaximoGrupo * (100 + toleranciaCupoPorcentaje) / 100;
    }

    // RF-23 y RF-24: dentro de la franja institucional del día y sin tocar el almuerzo.
    /**
     * Indica si una franja está dentro de la jornada institucional del día y no toca el almuerzo (RF-23 y
     * RF-24).
     *
     * @param franja la franja evaluada
     * @return {@code true} si la franja está permitida
     */
    public boolean franjaPermitida(FranjaHoraria franja) {
        boolean sabado = franja.getDiaSemana() == DiaSemana.SABADO;
        LocalTime inicioDia = sabado ? horaInicioSabado : horaInicioSemana;
        LocalTime finDia = sabado ? horaFinSabado : horaFinSemana;
        boolean dentroDelDia = !franja.getHoraInicio().isBefore(inicioDia) && !franja.getHoraFin().isAfter(finDia);
        boolean tocaAlmuerzo = franja.getHoraInicio().isBefore(finAlmuerzo) && inicioAlmuerzo.isBefore(franja.getHoraFin());
        return dentroDelDia && !tocaAlmuerzo;
    }

    /**
     * Obtiene el identificador de la fila de parámetros.
     *
     * @return el identificador de la fila de parámetros
     */
    public Integer getIdParametros() {
        return idParametros;
    }

    /**
     * Establece el identificador de la fila de parámetros.
     *
     * @param idParametros el identificador de la fila de parámetros
     */
    public void setIdParametros(Integer idParametros) {
        this.idParametros = idParametros;
    }

    /**
     * Obtiene el tamaño máximo de un grupo.
     *
     * @return el tamaño máximo de un grupo
     */
    public Integer getTamanoMaximoGrupo() {
        return tamanoMaximoGrupo;
    }

    /**
     * Establece el tamaño máximo de un grupo.
     *
     * @param tamanoMaximoGrupo el tamaño máximo de un grupo
     */
    public void setTamanoMaximoGrupo(Integer tamanoMaximoGrupo) {
        this.tamanoMaximoGrupo = tamanoMaximoGrupo;
    }

    /**
     * Obtiene el porcentaje de tolerancia sobre el tamaño máximo de grupo.
     *
     * @return el porcentaje de tolerancia sobre el tamaño máximo de grupo
     */
    public Integer getToleranciaCupoPorcentaje() {
        return toleranciaCupoPorcentaje;
    }

    /**
     * Establece el porcentaje de tolerancia sobre el tamaño máximo de grupo.
     *
     * @param toleranciaCupoPorcentaje el porcentaje de tolerancia sobre el tamaño máximo de grupo
     */
    public void setToleranciaCupoPorcentaje(Integer toleranciaCupoPorcentaje) {
        this.toleranciaCupoPorcentaje = toleranciaCupoPorcentaje;
    }

    /**
     * Obtiene el mínimo de estudiantes por grupo, o {@code null} si no está configurado.
     *
     * @return el mínimo de estudiantes por grupo, o {@code null} si no está configurado
     */
    public Integer getMinimoEstudiantesGrupo() {
        return minimoEstudiantesGrupo;
    }

    /**
     * Establece el mínimo de estudiantes por grupo, o {@code null} si no está configurado.
     *
     * @param minimoEstudiantesGrupo el mínimo de estudiantes por grupo, o {@code null} si no está configurado
     */
    public void setMinimoEstudiantesGrupo(Integer minimoEstudiantesGrupo) {
        this.minimoEstudiantesGrupo = minimoEstudiantesGrupo;
    }

    /**
     * Obtiene la hora de inicio de la jornada de lunes a viernes.
     *
     * @return la hora de inicio de la jornada de lunes a viernes
     */
    public LocalTime getHoraInicioSemana() {
        return horaInicioSemana;
    }

    /**
     * Establece la hora de inicio de la jornada de lunes a viernes.
     *
     * @param horaInicioSemana la hora de inicio de la jornada de lunes a viernes
     */
    public void setHoraInicioSemana(LocalTime horaInicioSemana) {
        this.horaInicioSemana = horaInicioSemana;
    }

    /**
     * Obtiene la hora de fin de la jornada de lunes a viernes.
     *
     * @return la hora de fin de la jornada de lunes a viernes
     */
    public LocalTime getHoraFinSemana() {
        return horaFinSemana;
    }

    /**
     * Establece la hora de fin de la jornada de lunes a viernes.
     *
     * @param horaFinSemana la hora de fin de la jornada de lunes a viernes
     */
    public void setHoraFinSemana(LocalTime horaFinSemana) {
        this.horaFinSemana = horaFinSemana;
    }

    /**
     * Obtiene la hora de inicio de la jornada del sábado.
     *
     * @return la hora de inicio de la jornada del sábado
     */
    public LocalTime getHoraInicioSabado() {
        return horaInicioSabado;
    }

    /**
     * Establece la hora de inicio de la jornada del sábado.
     *
     * @param horaInicioSabado la hora de inicio de la jornada del sábado
     */
    public void setHoraInicioSabado(LocalTime horaInicioSabado) {
        this.horaInicioSabado = horaInicioSabado;
    }

    /**
     * Obtiene la hora de fin de la jornada del sábado.
     *
     * @return la hora de fin de la jornada del sábado
     */
    public LocalTime getHoraFinSabado() {
        return horaFinSabado;
    }

    /**
     * Establece la hora de fin de la jornada del sábado.
     *
     * @param horaFinSabado la hora de fin de la jornada del sábado
     */
    public void setHoraFinSabado(LocalTime horaFinSabado) {
        this.horaFinSabado = horaFinSabado;
    }

    /**
     * Obtiene la hora de inicio de la franja de almuerzo.
     *
     * @return la hora de inicio de la franja de almuerzo
     */
    public LocalTime getInicioAlmuerzo() {
        return inicioAlmuerzo;
    }

    /**
     * Establece la hora de inicio de la franja de almuerzo.
     *
     * @param inicioAlmuerzo la hora de inicio de la franja de almuerzo
     */
    public void setInicioAlmuerzo(LocalTime inicioAlmuerzo) {
        this.inicioAlmuerzo = inicioAlmuerzo;
    }

    /**
     * Obtiene la hora de fin de la franja de almuerzo.
     *
     * @return la hora de fin de la franja de almuerzo
     */
    public LocalTime getFinAlmuerzo() {
        return finAlmuerzo;
    }

    /**
     * Establece la hora de fin de la franja de almuerzo.
     *
     * @param finAlmuerzo la hora de fin de la franja de almuerzo
     */
    public void setFinAlmuerzo(LocalTime finAlmuerzo) {
        this.finAlmuerzo = finAlmuerzo;
    }

    /**
     * Obtiene la duración obligatoria de cada sesión en minutos.
     *
     * @return la duración obligatoria de cada sesión en minutos
     */
    public Integer getDuracionSesionMinutos() {
        return duracionSesionMinutos;
    }

    /**
     * Establece la duración obligatoria de cada sesión en minutos.
     *
     * @param duracionSesionMinutos la duración obligatoria de cada sesión en minutos
     */
    public void setDuracionSesionMinutos(Integer duracionSesionMinutos) {
        this.duracionSesionMinutos = duracionSesionMinutos;
    }

    /**
     * Obtiene el máximo de sesiones semanales por grupo.
     *
     * @return el máximo de sesiones semanales por grupo
     */
    public Integer getMaxSesionesSemanales() {
        return maxSesionesSemanales;
    }

    /**
     * Establece el máximo de sesiones semanales por grupo.
     *
     * @param maxSesionesSemanales el máximo de sesiones semanales por grupo
     */
    public void setMaxSesionesSemanales(Integer maxSesionesSemanales) {
        this.maxSesionesSemanales = maxSesionesSemanales;
    }

    /**
     * Obtiene los créditos mínimos para matricular, o {@code null} si no aplica.
     *
     * @return los créditos mínimos para matricular, o {@code null} si no aplica
     */
    public Integer getCreditosMinimos() {
        return creditosMinimos;
    }

    /**
     * Establece los créditos mínimos para matricular, o {@code null} si no aplica.
     *
     * @param creditosMinimos los créditos mínimos para matricular, o {@code null} si no aplica
     */
    public void setCreditosMinimos(Integer creditosMinimos) {
        this.creditosMinimos = creditosMinimos;
    }

    /**
     * Obtiene los créditos máximos para matricular, o {@code null} si no aplica.
     *
     * @return los créditos máximos para matricular, o {@code null} si no aplica
     */
    public Integer getCreditosMaximos() {
        return creditosMaximos;
    }

    /**
     * Establece los créditos máximos para matricular, o {@code null} si no aplica.
     *
     * @param creditosMaximos los créditos máximos para matricular, o {@code null} si no aplica
     */
    public void setCreditosMaximos(Integer creditosMaximos) {
        this.creditosMaximos = creditosMaximos;
    }

    /**
     * Obtiene los días de anticipación de los avisos de apertura y cierre de inscripción (HU-40).
     *
     * @return los días de anticipación de los avisos de apertura y cierre de inscripción (HU-40)
     */
    public Integer getDiasAnticipacionAviso() {
        return diasAnticipacionAviso;
    }

    /**
     * Establece los días de anticipación de los avisos de apertura y cierre de inscripción (HU-40).
     *
     * @param diasAnticipacionAviso los días de anticipación de los avisos de apertura y cierre de inscripción
     *         (HU-40)
     */
    public void setDiasAnticipacionAviso(Integer diasAnticipacionAviso) {
        this.diasAnticipacionAviso = diasAnticipacionAviso;
    }

    /**
     * Obtiene el porcentaje de la carga máxima bajo el cual un docente se considera subutilizado (HU-08).
     *
     * @return el porcentaje de la carga máxima bajo el cual un docente se considera subutilizado (HU-08)
     */
    public Integer getUmbralSubutilizacionPorcentaje() {
        return umbralSubutilizacionPorcentaje;
    }

    /**
     * Establece el porcentaje de la carga máxima bajo el cual un docente se considera subutilizado (HU-08).
     *
     * @param umbralSubutilizacionPorcentaje el porcentaje de la carga máxima bajo el cual un docente se
     *         considera subutilizado (HU-08)
     */
    public void setUmbralSubutilizacionPorcentaje(Integer umbralSubutilizacionPorcentaje) {
        this.umbralSubutilizacionPorcentaje = umbralSubutilizacionPorcentaje;
    }
}
