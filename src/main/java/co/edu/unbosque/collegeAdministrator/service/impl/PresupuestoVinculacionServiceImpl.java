package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.entity.PresupuestoVinculacion;
import co.edu.unbosque.collegeAdministrator.entity.TipoVinculacion;
import co.edu.unbosque.collegeAdministrator.repository.PresupuestoVinculacionRepository;
import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PresupuestoVinculacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.TipoVinculacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de las operaciones sobre el presupuesto por vinculación.
 */
@Service
@Transactional
public class PresupuestoVinculacionServiceImpl extends GenericServiceImpl<PresupuestoVinculacion, Integer>
        implements PresupuestoVinculacionServiceAPI {

    /** El repositorio de presupuestos por vinculación. */
    private final PresupuestoVinculacionRepository presupuestoVinculacionRepository;
    /** El servicio de gestión de periodos académicos. */
    private final PeriodoAcademicoServiceAPI periodoAcademicoService;
    /** El servicio de tipos de vinculación docente. */
    private final TipoVinculacionServiceAPI tipoVinculacionService;

    /**
     * Crea una instancia de {@code PresupuestoVinculacionServiceImpl} con las dependencias requeridas.
     *
     * @param presupuestoVinculacionRepository el repositorio de presupuestos por vinculación
     * @param periodoAcademicoService el servicio de gestión de periodos académicos
     * @param tipoVinculacionService el servicio de tipos de vinculación docente
     */
    public PresupuestoVinculacionServiceImpl(PresupuestoVinculacionRepository presupuestoVinculacionRepository,
                                             PeriodoAcademicoServiceAPI periodoAcademicoService,
                                             TipoVinculacionServiceAPI tipoVinculacionService) {
        this.presupuestoVinculacionRepository = presupuestoVinculacionRepository;
        this.periodoAcademicoService = periodoAcademicoService;
        this.tipoVinculacionService = tipoVinculacionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<PresupuestoVinculacion, Integer> getRepository() {
        return presupuestoVinculacionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PresupuestoVinculacion registrar(Integer idPeriodo, Integer idTipoVinculacion, Integer horasPresupuestadas) {
        if (horasPresupuestadas == null || horasPresupuestadas < 0) {
            throw new ReglaNegocioException("Las horas presupuestadas no pueden ser negativas");
        }
        PeriodoAcademico periodo = periodoAcademicoService.buscarPorId(idPeriodo);
        if (periodo.estaCerrado()) {
            throw new ReglaNegocioException("El periodo " + periodo.getCodigo() + " está cerrado y es de solo lectura");
        }
        TipoVinculacion tipo = tipoVinculacionService.buscarPorId(idTipoVinculacion);
        PresupuestoVinculacion presupuesto = presupuestoVinculacionRepository
                .findByPeriodo_IdPeriodoAndTipoVinculacion_IdTipoVinculacion(idPeriodo, idTipoVinculacion)
                .orElseGet(() -> new PresupuestoVinculacion(periodo, tipo, horasPresupuestadas));
        presupuesto.setHorasPresupuestadas(horasPresupuestadas);
        return presupuestoVinculacionRepository.save(presupuesto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<PresupuestoVinculacion> listarPorPeriodo(Integer idPeriodo) {
        return presupuestoVinculacionRepository.findByPeriodo_IdPeriodo(idPeriodo);
    }
}
