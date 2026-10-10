package co.edu.unbosque.collegeAdministrator.controller;

/**
 * Expresiones de @PreAuthorize por rol (HU-02). "Solo sus propios datos" (HU-53) se valida además en cada
 * endpoint con UsuarioActual.
 */
public final class Permisos {

    public static final String AUTENTICADO = "isAuthenticated()";
    public static final String ADMIN = "hasRole('ADMINISTRADOR')";
    public static final String DIRECCION = "hasAnyRole('DIRECTOR_PROGRAMA','ADMINISTRADOR')";
    public static final String DIRECTIVOS = "hasAnyRole('DIRECTOR_PROGRAMA','DECANO','ADMINISTRADOR')";
    public static final String PRESUPUESTO = "hasAnyRole('DECANO','ADMINISTRADOR')";
    public static final String DOCENTE = "hasRole('DOCENTE')";
    public static final String DOCENTE_O_ADMIN = "hasAnyRole('DOCENTE','ADMINISTRADOR')";
    public static final String ESTUDIANTE = "hasRole('ESTUDIANTE')";
    public static final String ESTUDIANTE_O_ADMIN = "hasAnyRole('ESTUDIANTE','ADMINISTRADOR')";

    private Permisos() {
    }
}
