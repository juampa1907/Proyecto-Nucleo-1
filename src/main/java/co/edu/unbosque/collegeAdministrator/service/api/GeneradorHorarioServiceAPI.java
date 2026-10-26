package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.ResultadoGeneracion;


public interface GeneradorHorarioServiceAPI {

    ResultadoGeneracion generarPropuesta(Integer idPeriodo);
}
