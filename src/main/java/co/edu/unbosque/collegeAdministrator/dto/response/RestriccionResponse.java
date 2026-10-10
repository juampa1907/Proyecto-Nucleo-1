package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;

import java.time.LocalTime;

public record RestriccionResponse(Integer idRestriccion, DiaSemana dia, LocalTime horaInicio, LocalTime horaFin) {
}
