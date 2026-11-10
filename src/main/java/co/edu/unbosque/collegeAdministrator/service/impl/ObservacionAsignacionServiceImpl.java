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

/**
 * Implementación de las operaciones sobre las observaciones de asignación.
 */
@Service
@Transactional
public class ObservacionAsignacionServiceImpl extends GenericServiceImpl<ObservacionAsignacion, Integer>
        implements ObservacionAsignacionServiceAPI {

    /** El repositorio de observaciones de asignación. */
    private final ObservacionAsignacionRepository observacionAsignacionRepository;
    /** El servicio de gestión de grupos y sesiones. */
    private final GrupoServiceAPI grupoService;

    /**
     * Crea una instancia de {@code ObservacionAsignacionServiceImpl} con las dependencias requeridas.
     *
     * @param observacionAsignacionRepository el repositorio de observaciones de asignación
     * @param grupoService el servicio de gestión de grupos y sesiones
     */
    public ObservacionAsignacionServiceImpl(ObservacionAsignacionRepository observacionAsignacionRepository,
                                            GrupoServiceAPI grupoService) {
        this.observacionAsignacionRepository = observacionAsignacionRepository;
        this.grupoService = grupoService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<ObservacionAsignacion, Integer> getRepository() {
        return observacionAsignacionRepository;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ObservacionAsignacion resolver(Integer idObservacion) {
        ObservacionAsignacion observacion = buscarPorId(idObservacion);
        observacion.setEstado(EstadoObservacion.RESUELTA);
        return observacion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ObservacionAsignacion> listarPendientes(Integer idPeriodo) {
        return observacionAsignacionRepository.findByGrupo_Periodo_IdPeriodoAndEstado(idPeriodo, EstadoObservacion.PENDIENTE);
    }
}
