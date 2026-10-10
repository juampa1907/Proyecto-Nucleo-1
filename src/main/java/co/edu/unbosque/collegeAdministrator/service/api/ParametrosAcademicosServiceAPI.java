package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;

/**
 * No extiende GenericServiceAPI: los parámetros son una sola fila que se consulta y se actualiza,
 * no se listan ni se eliminan.
 */
public interface ParametrosAcademicosServiceAPI {

    ParametrosAcademicos obtener();

    ParametrosAcademicos actualizar(ParametrosAcademicos parametros);
}
