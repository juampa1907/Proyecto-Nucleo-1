package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalTime;


public record ParametrosResponse(Integer tamanoMaximoGrupo, Integer toleranciaCupoPorcentaje, int cupoMaximoPermitido,
                                 Integer minimoEstudiantesGrupo, LocalTime horaInicioSemana, LocalTime horaFinSemana,
                                 LocalTime horaInicioSabado, LocalTime horaFinSabado, LocalTime inicioAlmuerzo,
                                 LocalTime finAlmuerzo, Integer duracionSesionMinutos, Integer maxSesionesSemanales,
                                 Integer creditosMinimos, Integer creditosMaximos, Integer diasAnticipacionAviso,
                                 Integer umbralSubutilizacionPorcentaje) {
}
