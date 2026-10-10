package co.edu.unbosque.collegeAdministrator.service.api;

/**
 * HU-37: comprobante de la carga académica inscrita, en PDF.
 */
public interface ComprobanteServiceAPI {

    byte[] generarComprobante(Integer idEstudiante, Integer idPeriodo);
}
