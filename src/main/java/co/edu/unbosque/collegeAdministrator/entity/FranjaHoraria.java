package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Objeto embebible que representa un intervalo horario dentro de un día de la semana.
 */
@Embeddable
public class FranjaHoraria {

    /** El día de la semana de la franja. */
    @Enumerated(EnumType.STRING)
    @Column(name = "DIA_SEMANA", nullable = false, length = 15)
    private DiaSemana diaSemana;

    /** La hora de inicio de la franja. */
    @Column(name = "HORA_INICIO", nullable = false)
    private LocalTime horaInicio;

    /** La hora de fin de la franja. */
    @Column(name = "HORA_FIN", nullable = false)
    private LocalTime horaFin;

    /**
     * Constructor vacío requerido por JPA.
     */
    protected FranjaHoraria() {
    }

    /**
     * Crea una franja horaria validada.
     *
     * @param diaSemana el día de la semana de la franja
     * @param horaInicio la hora de inicio de la franja
     * @param horaFin la hora de fin de la franja
     * @throws IllegalArgumentException si falta algún dato o la hora de fin no es posterior a la de inicio
     */
    public FranjaHoraria(DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        if (diaSemana == null || horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("El día y las horas de la franja son obligatorios");
        }
        if (!horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    /**
     * Obtiene el día de la semana de la franja.
     *
     * @return el día de la semana de la franja
     */
    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    /**
     * Obtiene la hora de inicio de la franja.
     *
     * @return la hora de inicio de la franja
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Obtiene la hora de fin de la franja.
     *
     * @return la hora de fin de la franja
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * Indica si esta franja se solapa con otra el mismo día. Dos franjas contiguas no se consideran cruzadas.
     *
     * @param otra la franja con la que se compara
     * @return {@code true} si las franjas se solapan
     */
    public boolean seCruzaCon(FranjaHoraria otra) {
        return diaSemana == otra.diaSemana
                && horaInicio.isBefore(otra.horaFin)
                && otra.horaInicio.isBefore(horaFin);
    }

    /**
     * Calcula la duración de la franja.
     *
     * @return la duración en minutos
     */
    public long calcularMinutos() {
        return Duration.between(horaInicio, horaFin).toMinutes();
    }

    /**
     * Calcula la duración de la franja en horas.
     *
     * @return la duración en horas, con decimales
     */
    public double calcularHoras() {
        return calcularMinutos() / 60.0;
    }

    /**
     * Devuelve la franja en formato legible.
     *
     * @return el día seguido del intervalo {@code inicio-fin}
     */
    @Override
    public String toString() {
        return diaSemana + " " + horaInicio + "-" + horaFin;
    }
}
