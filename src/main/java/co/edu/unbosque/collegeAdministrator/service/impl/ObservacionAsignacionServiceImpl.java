package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.ObservacionAsignacion;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoObservacion;
import co.edu.unbosque.collegeAdministrator.repository.ObservacionAsignacionRepository;
import co.edu.unbosque.collegeAdministrator.service.api.GrupoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ObservacionAsignacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ObservacionAsignacionServiceImpl extends GenericServiceImpl<ObservacionAsignacion, Integer>
        implements ObservacionAsignacionServiceAPI {

    private final ObservacionAsignacionRepository observacionAsignacionRepository;
    private final GrupoServiceAPI grupoService;

    public ObservacionAsignacionServiceImpl(ObservacionAsignacionRepository observacionAsignacionRepository,
                                            GrupoServiceAPI grupoService) {
        this.observacionAsignacionRepository = observacionAsignacionRepository;
        this.grupoService = grupoService;
    }

    @Override
    protected JpaRepository<ObservacionAsignacion, Integer> getRepository() {
        return observacionAsignacionRepository;
    }

    // HU-55: el docente revisa su asignación en borrador y deja una observación para el Director.
    @Override
    public ObservacionAsignacion registrar(Integer idGrupo, Integer idDocente, String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ReglaNegocioException("La observación no puede estar vacía");
        }
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        if (grupo.getDocente() == null || !grupo.getDocente().getIdUsuario().equals(idDocente)) {
            throw new ReglaNegocioException("Solo el docente asignado al grupo puede registrar observaciones");
        }
        if (grupo.getPeriodo().horarioPublicado()) {
            throw new ReglaNegocioException("El horario ya fue publicado; las observaciones se hacen en borrador");
        }
        return observacionAsignacionRepository.save(new ObservacionAsignacion(grupo, grupo.getDocente(), texto));
    }

    @Override
    public ObservacionAsignacion resolver(Integer idObservacion) {
        ObservacionAsignacion observacion = buscarPorId(idObservacion);
        observacion.setEstado(EstadoObservacion.RESUELTA);
        return observacion;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObservacionAsignacion> listarPendientes(Integer idPeriodo) {
        return observacionAsignacionRepository.findByGrupo_Periodo_IdPeriodoAndEstado(idPeriodo, EstadoObservacion.PENDIENTE);
    }
}
