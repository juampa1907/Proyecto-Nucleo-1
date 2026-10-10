package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Duration;
import java.time.LocalTime;

@Embeddable
public class FranjaHoraria {

    @Enumerated(EnumType.STRING)
    @Column(name = "DIA_SEMANA", nullable = false, length = 15)
    private DiaSemana diaSemana;

    @Column(name = "HORA_INICIO", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "HORA_FIN", nullable = false)
    private LocalTime horaFin;

    protected FranjaHoraria() {
    }

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

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public boolean seCruzaCon(FranjaHoraria otra) {
        return diaSemana == otra.diaSemana
                && horaInicio.isBefore(otra.horaFin)
                && otra.horaInicio.isBefore(horaFin);
    }

    public long calcularMinutos() {
        return Duration.between(horaInicio, horaFin).toMinutes();
    }

    public double calcularHoras() {
        return calcularMinutos() / 60.0;
    }

    @Override
    public String toString() {
        return diaSemana + " " + horaInicio + "-" + horaFin;
    }
}
