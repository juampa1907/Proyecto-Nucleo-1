package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.time.LocalDate;

public interface PeriodoAcademicoServiceAPI extends GenericServiceAPI<PeriodoAcademico, Integer> {

    PeriodoAcademico cerrar(Integer idPeriodo);

    PeriodoAcademico publicarHorario(Integer idPeriodo);

    /**
     * HU-40: lo invoca la tarea programada diaria.
     */
    void enviarAvisosInscripcion(LocalDate hoy);
}
