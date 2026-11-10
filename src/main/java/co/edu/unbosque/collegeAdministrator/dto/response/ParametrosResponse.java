package co.edu.unbosque.collegeAdministrator.dto.response;

import java.time.LocalTime;


/**
 * Parámetros académicos institucionales.
 *
 * @param tamanoMaximoGrupo el tamaño máximo de un grupo
 * @param toleranciaCupoPorcentaje el porcentaje de tolerancia sobre el tamaño máximo de grupo
 * @param cupoMaximoPermitido el cupo máximo con la tolerancia aplicada
 * @param minimoEstudiantesGrupo el mínimo de estudiantes por grupo, o {@code null} si no está configurado
 * @param horaInicioSemana la hora de inicio de la jornada de lunes a viernes
 * @param horaFinSemana la hora de fin de la jornada de lunes a viernes
 * @param horaInicioSabado la hora de inicio de la jornada del sábado
 * @param horaFinSabado la hora de fin de la jornada del sábado
 * @param inicioAlmuerzo la hora de inicio de la franja de almuerzo
 * @param finAlmuerzo la hora de fin de la franja de almuerzo
 * @param duracionSesionMinutos la duración de cada sesión en minutos
 * @param maxSesionesSemanales el máximo de sesiones semanales por grupo
 * @param creditosMinimos los créditos mínimos para matricular, o {@code null} si no aplica
 * @param creditosMaximos los créditos máximos para matricular, o {@code null} si no aplica
 * @param diasAnticipacionAviso los días de anticipación de los avisos de inscripción
 * @param umbralSubutilizacionPorcentaje el porcentaje de la carga máxima bajo el cual un docente se considera
 *         subutilizado
 */
public record ParametrosResponse(Integer tamanoMaximoGrupo, Integer toleranciaCupoPorcentaje, int cupoMaximoPermitido,
                                 Integer minimoEstudiantesGrupo, LocalTime horaInicioSemana, LocalTime horaFinSemana,
                                 LocalTime horaInicioSabado, LocalTime horaFinSabado, LocalTime inicioAlmuerzo,
                                 LocalTime finAlmuerzo, Integer duracionSesionMinutos, Integer maxSesionesSemanales,
                                 Integer creditosMinimos, Integer creditosMaximos, Integer diasAnticipacionAviso,
                                 Integer umbralSubutilizacionPorcentaje) {
}
