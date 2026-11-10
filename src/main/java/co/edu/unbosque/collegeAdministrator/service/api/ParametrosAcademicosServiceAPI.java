package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;


/**
 * Operaciones sobre los parámetros académicos institucionales.
 */
public interface ParametrosAcademicosServiceAPI {

    /**
     * Obtiene los parámetros y los crea con sus valores por defecto si aún no existen.
     *
     * @return los parámetros vigentes
     */
    ParametrosAcademicos obtener();

    /**
     * Valida y reemplaza los parámetros vigentes.
     *
     * @param parametros los nuevos valores
     * @return los parámetros guardados
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si algún valor no es
     *         válido
     */
    ParametrosAcademicos actualizar(ParametrosAcademicos parametros);
}
