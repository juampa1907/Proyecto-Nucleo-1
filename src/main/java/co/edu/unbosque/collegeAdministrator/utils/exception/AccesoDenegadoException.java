package co.edu.unbosque.collegeAdministrator.utils.exception;


/**
 * Excepción lanzada cuando un usuario intenta acceder a información o acciones que no le corresponden; se
 * traduce en HTTP 403.
 */
public class AccesoDenegadoException extends RuntimeException {

    /** La versión de serialización de la excepción. */
    private static final long serialVersionUID = 1L;

    /**
     * Crea una instancia de {@code AccesoDenegadoException} con los datos indicados.
     *
     * @param mensaje el mensaje del error
     */
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
