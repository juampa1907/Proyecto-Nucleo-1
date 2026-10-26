package co.edu.unbosque.collegeAdministrator.config;

import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class AvisosInscripcionScheduler {

    private final PeriodoAcademicoServiceAPI periodoAcademicoService;

    public AvisosInscripcionScheduler(PeriodoAcademicoServiceAPI periodoAcademicoService) {
        this.periodoAcademicoService = periodoAcademicoService;
    }

    @Scheduled(cron = "${app.avisos.cron:0 0 7 * * *}")
    public void enviarAvisos() {
        periodoAcademicoService.enviarAvisosInscripcion(LocalDate.now());
    }
}
