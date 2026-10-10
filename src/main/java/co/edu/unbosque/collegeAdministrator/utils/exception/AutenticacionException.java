package co.edu.unbosque.collegeAdministrator.utils.exception;

/**
 * Credenciales inválidas, usuario bloqueado o sin rol (luego → HTTP 401).
 */
public class AutenticacionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AutenticacionException(String mensaje) {
        super(mensaje);
    }
}
