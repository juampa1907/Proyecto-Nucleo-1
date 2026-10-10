package co.edu.unbosque.collegeAdministrator.dto.reporte;

/**
 * HU-08 / HU-43: carga de un docente en un periodo.
 *
 * @param horasMaximas null si el docente no tiene tipo de vinculación.
 */
public record CargaDocente(Integer idDocente, String username, String tipoVinculacion, Integer horasMaximas,
                           double horasAsignadas, IndicadorCarga indicador) {
}
