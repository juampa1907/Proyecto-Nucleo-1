package co.edu.unbosque.collegeAdministrator.utils.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no existe; se traduce en HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    /** La versión de serialización de la excepción. */
    private static final long serialVersionUID = 1L;

    /**
     * Crea una instancia de {@code RecursoNoEncontradoException} con los datos indicados.
     *
     * @param mensaje el mensaje del error
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
