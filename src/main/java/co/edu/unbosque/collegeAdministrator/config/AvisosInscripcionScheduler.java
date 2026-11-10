package co.edu.unbosque.collegeAdministrator.config;

import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


/**
 * Tarea programada que envía los avisos de apertura y cierre de inscripción (HU-40).
 */
@Component
public class AvisosInscripcionScheduler {

    /** El servicio de gestión de periodos académicos. */
    private final PeriodoAcademicoServiceAPI periodoAcademicoService;

    /**
     * Crea una instancia de {@code AvisosInscripcionScheduler} con las dependencias requeridas.
     *
     * @param periodoAcademicoService el servicio de gestión de periodos académicos
     */
    public AvisosInscripcionScheduler(PeriodoAcademicoServiceAPI periodoAcademicoService) {
        this.periodoAcademicoService = periodoAcademicoService;
    }

    /**
     * Envía los avisos que correspondan a la fecha actual. Se ejecuta según la expresión cron {@code
     * app.avisos.cron}, por defecto todos los días a las 7:00.
     */
    @Scheduled(cron = "${app.avisos.cron:0 0 7 * * *}")
    public void enviarAvisos() {
        periodoAcademicoService.enviarAvisosInscripcion(LocalDate.now());
    }
}
