package co.edu.unbosque.collegeAdministrator.utils.exception;

/**
 * Excepción lanzada cuando una operación incumple una regla de negocio; se traduce en HTTP 400.
 */
public class ReglaNegocioException extends RuntimeException {

    /** La versión de serialización de la excepción. */
    private static final long serialVersionUID = 1L;

    /**
     * Crea una instancia de {@code ReglaNegocioException} con los datos indicados.
     *
     * @param mensaje el mensaje del error
     */
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
