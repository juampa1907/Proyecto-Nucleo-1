package co.edu.unbosque.collegeAdministrator.utils.exception;


/**
 * Excepción lanzada cuando no se puede autenticar al usuario; se traduce en HTTP 401.
 */
public class AutenticacionException extends RuntimeException {

    /** La versión de serialización de la excepción. */
    private static final long serialVersionUID = 1L;

    /**
     * Crea una instancia de {@code AutenticacionException} con los datos indicados.
     *
     * @param mensaje el mensaje del error
     */
    public AutenticacionException(String mensaje) {
        super(mensaje);
    }
}
