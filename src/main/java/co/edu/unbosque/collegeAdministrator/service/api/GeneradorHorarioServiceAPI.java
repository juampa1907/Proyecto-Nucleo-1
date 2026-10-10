package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.ResultadoGeneracion;

/**
 * HU-24 / ESP-NF-01: propuesta automática de horario con una heurística greedy (no garantiza el óptimo global).
 */
public interface GeneradorHorarioServiceAPI {

    ResultadoGeneracion generarPropuesta(Integer idPeriodo);
}
