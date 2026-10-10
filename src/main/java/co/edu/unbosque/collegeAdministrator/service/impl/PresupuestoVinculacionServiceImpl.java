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

@Service
@Transactional
public class PresupuestoVinculacionServiceImpl extends GenericServiceImpl<PresupuestoVinculacion, Integer>
        implements PresupuestoVinculacionServiceAPI {

    private final PresupuestoVinculacionRepository presupuestoVinculacionRepository;
    private final PeriodoAcademicoServiceAPI periodoAcademicoService;
    private final TipoVinculacionServiceAPI tipoVinculacionService;

    public PresupuestoVinculacionServiceImpl(PresupuestoVinculacionRepository presupuestoVinculacionRepository,
                                             PeriodoAcademicoServiceAPI periodoAcademicoService,
                                             TipoVinculacionServiceAPI tipoVinculacionService) {
        this.presupuestoVinculacionRepository = presupuestoVinculacionRepository;
        this.periodoAcademicoService = periodoAcademicoService;
        this.tipoVinculacionService = tipoVinculacionService;
    }

    @Override
    protected JpaRepository<PresupuestoVinculacion, Integer> getRepository() {
        return presupuestoVinculacionRepository;
    }

    // Si ya hay presupuesto para ese periodo y tipo, se actualiza en lugar de duplicarlo.
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

    @Override
    @Transactional(readOnly = true)
    public List<PresupuestoVinculacion> listarPorPeriodo(Integer idPeriodo) {
        return presupuestoVinculacionRepository.findByPeriodo_IdPeriodo(idPeriodo);
    }
}
