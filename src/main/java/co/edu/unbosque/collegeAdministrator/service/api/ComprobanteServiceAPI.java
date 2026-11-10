package co.edu.unbosque.collegeAdministrator.service.api;


/**
 * Generación de comprobantes de carga académica (HU-37).
 */
public interface ComprobanteServiceAPI {

    /**
     * Genera el comprobante en PDF de la carga de un estudiante y guarda su código de verificación en la
     * matrícula, si existe.
     *
     * @param idEstudiante el identificador del estudiante
     * @param idPeriodo el identificador del periodo académico
     * @return el contenido del archivo PDF
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el estudiante no
     *         tiene materias en el periodo
     */
    byte[] generarComprobante(Integer idEstudiante, Integer idPeriodo);
}
