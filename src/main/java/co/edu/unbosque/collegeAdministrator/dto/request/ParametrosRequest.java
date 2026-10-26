package co.edu.unbosque.collegeAdministrator.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;


public record ParametrosRequest(
        @NotNull Integer tamanoMaximoGrupo,
        @NotNull Integer toleranciaCupoPorcentaje,
        Integer minimoEstudiantesGrupo,
        @NotNull LocalTime horaInicioSemana,
        @NotNull LocalTime horaFinSemana,
        @NotNull LocalTime horaInicioSabado,
        @NotNull LocalTime horaFinSabado,
        @NotNull LocalTime inicioAlmuerzo,
        @NotNull LocalTime finAlmuerzo,
        @NotNull Integer duracionSesionMinutos,
        @NotNull Integer maxSesionesSemanales,
        Integer creditosMinimos,
        Integer creditosMaximos,
        @NotNull Integer diasAnticipacionAviso,
        @NotNull Integer umbralSubutilizacionPorcentaje) {
}
