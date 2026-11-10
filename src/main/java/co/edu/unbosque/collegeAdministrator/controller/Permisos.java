package co.edu.unbosque.collegeAdministrator.controller;


/**
 * Expresiones de autorización usadas con {@code @PreAuthorize} en los controladores.
 */
public final class Permisos {

    /** Cualquier usuario autenticado. */
    public static final String AUTENTICADO = "isAuthenticated()";
    /** Solo administradores. */
    public static final String ADMIN = "hasRole('ADMINISTRADOR')";
    /** Directores de programa y administradores. */
    public static final String DIRECCION = "hasAnyRole('DIRECTOR_PROGRAMA','ADMINISTRADOR')";
    /** Directores de programa, decanos y administradores. */
    public static final String DIRECTIVOS = "hasAnyRole('DIRECTOR_PROGRAMA','DECANO','ADMINISTRADOR')";
    /** Decanos y administradores. */
    public static final String PRESUPUESTO = "hasAnyRole('DECANO','ADMINISTRADOR')";
    /** Solo docentes. */
    public static final String DOCENTE = "hasRole('DOCENTE')";
    /** Docentes y administradores. */
    public static final String DOCENTE_O_ADMIN = "hasAnyRole('DOCENTE','ADMINISTRADOR')";
    /** Solo estudiantes. */
    public static final String ESTUDIANTE = "hasRole('ESTUDIANTE')";
    /** Estudiantes y administradores. */
    public static final String ESTUDIANTE_O_ADMIN = "hasAnyRole('ESTUDIANTE','ADMINISTRADOR')";

    /**
     * Constructor privado: esta clase no debe instanciarse.
     */
    private Permisos() {
    }
}
