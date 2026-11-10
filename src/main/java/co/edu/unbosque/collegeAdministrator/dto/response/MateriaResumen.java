package co.edu.unbosque.collegeAdministrator.dto.response;

/**
 * Resumen de una materia.
 *
 * @param idMateria el identificador de la materia
 * @param codigo el código de la materia
 * @param nombre el nombre de la materia
 */
public record MateriaResumen(Integer idMateria, String codigo, String nombre) {
}
