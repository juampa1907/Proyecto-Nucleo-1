package co.edu.unbosque.collegeAdministrator.utils.exception;

/**
 * HU-53: el usuario autenticado intenta ver o modificar datos que no le pertenecen (luego → HTTP 403).
 */
public class AccesoDenegadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
