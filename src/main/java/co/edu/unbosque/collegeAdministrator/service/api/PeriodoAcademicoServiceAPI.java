package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.time.LocalDate;

/**
 * Operaciones de negocio sobre los periodos académicos.
 */
public interface PeriodoAcademicoServiceAPI extends GenericServiceAPI<PeriodoAcademico, Integer> {

    /**
     * Cierra un periodo y lo deja de solo lectura.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return el periodo cerrado
     */
    PeriodoAcademico cerrar(Integer idPeriodo);

    /**
     * Publica el horario de un periodo y lo notifica a los docentes.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return el periodo actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si hay observaciones
     *         pendientes, no hay grupos abiertos o algún grupo está incompleto
     */
    PeriodoAcademico publicarHorario(Integer idPeriodo);

    /**
     * Envía los avisos pendientes de apertura y cierre de inscripción a los estudiantes (HU-40).
     *
     * @param hoy la fecha de referencia
     */
    void enviarAvisosInscripcion(LocalDate hoy);
}
