package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.DetalleHorario;
import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoHorario;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoObservacion;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoPeriodo;
import co.edu.unbosque.collegeAdministrator.repository.EstudianteRepository;
import co.edu.unbosque.collegeAdministrator.repository.GrupoRepository;
import co.edu.unbosque.collegeAdministrator.repository.ObservacionAsignacionRepository;
import co.edu.unbosque.collegeAdministrator.repository.PeriodoAcademicoRepository;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.NotificacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ParametrosAcademicosServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PeriodoAcademicoServiceImpl extends GenericServiceImpl<PeriodoAcademico, Integer>
        implements PeriodoAcademicoServiceAPI {

    private final PeriodoAcademicoRepository periodoAcademicoRepository;
    private final GrupoRepository grupoRepository;
    private final ObservacionAsignacionRepository observacionAsignacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final ParametrosAcademicosServiceAPI parametrosAcademicosService;
    private final NotificacionServiceAPI notificacionService;
    private final BitacoraServiceAPI bitacoraService;

    public PeriodoAcademicoServiceImpl(PeriodoAcademicoRepository periodoAcademicoRepository,
                                       GrupoRepository grupoRepository,
                                       ObservacionAsignacionRepository observacionAsignacionRepository,
                                       EstudianteRepository estudianteRepository,
                                       ParametrosAcademicosServiceAPI parametrosAcademicosService,
                                       NotificacionServiceAPI notificacionService,
                                       BitacoraServiceAPI bitacoraService) {
        this.periodoAcademicoRepository = periodoAcademicoRepository;
        this.grupoRepository = grupoRepository;
        this.observacionAsignacionRepository = observacionAsignacionRepository;
        this.estudianteRepository = estudianteRepository;
        this.parametrosAcademicosService = parametrosAcademicosService;
        this.notificacionService = notificacionService;
        this.bitacoraService = bitacoraService;
    }

    @Override
    protected JpaRepository<PeriodoAcademico, Integer> getRepository() {
        return periodoAcademicoRepository;
    }

    @Override
    public PeriodoAcademico guardar(PeriodoAcademico periodo) {
        if (periodo.getFechaInicio() == null || periodo.getFechaFin() == null
                || periodo.getFechaAperturaInscripcion() == null || periodo.getFechaCierreInscripcion() == null) {
            throw new ReglaNegocioException("Todas las fechas del periodo son obligatorias");
        }
        if (!periodo.getFechaInicio().isBefore(periodo.getFechaFin())) {
            throw new ReglaNegocioException("La fecha de inicio del periodo debe ser anterior a la fecha de fin");
        }
        if (periodo.getFechaAperturaInscripcion().isAfter(periodo.getFechaCierreInscripcion())) {
            throw new ReglaNegocioException("La apertura de inscripción no puede ser posterior a su cierre");
        }
        if (periodo.getIdPeriodo() == null) {
            if (periodoAcademicoRepository.existsByCodigo(periodo.getCodigo())) {
                throw new ReglaNegocioException("Ya existe el periodo " + periodo.getCodigo());
            }
        } else if (buscarPorId(periodo.getIdPeriodo()).estaCerrado()) {
            throw new ReglaNegocioException("El periodo " + periodo.getCodigo() + " está cerrado y es de solo lectura");
        }
        return super.guardar(periodo);
    }

    @Override
    public PeriodoAcademico cerrar(Integer idPeriodo) {
        PeriodoAcademico periodo = buscarPorId(idPeriodo);
        periodo.setEstado(EstadoPeriodo.CERRADO);
        bitacoraService.registrar("CERRAR_PERIODO", periodo.getCodigo());
        return periodo;
    }

    // HU-28: solo se publica un horario completo y sin observaciones pendientes (HU-55).
    @Override
    public PeriodoAcademico publicarHorario(Integer idPeriodo) {
        PeriodoAcademico periodo = buscarPorId(idPeriodo);
        if (periodo.estaCerrado()) {
            throw new ReglaNegocioException("El periodo " + periodo.getCodigo() + " está cerrado");
        }
        if (periodo.horarioPublicado()) {
            throw new ReglaNegocioException("El horario del periodo " + periodo.getCodigo() + " ya fue publicado");
        }
        if (observacionAsignacionRepository.existsByGrupo_Periodo_IdPeriodoAndEstado(idPeriodo, EstadoObservacion.PENDIENTE)) {
            throw new ReglaNegocioException("Hay observaciones de docentes pendientes por resolver");
        }
        List<Grupo> grupos = grupoRepository.findByPeriodo_IdPeriodoAndEstado(idPeriodo, EstadoGrupo.ABIERTO);
        if (grupos.isEmpty()) {
            throw new ReglaNegocioException("El periodo " + periodo.getCodigo() + " no tiene grupos abiertos");
        }
        for (Grupo grupo : grupos) {
            String nombreGrupo = grupo.getMateria().getNombre() + " (grupo " + grupo.getCodigo() + ")";
            if (grupo.getDocente() == null) {
                throw new ReglaNegocioException(nombreGrupo + " no tiene docente asignado");
            }
            if (grupo.getSesiones().isEmpty()) {
                throw new ReglaNegocioException(nombreGrupo + " no tiene sesiones programadas");
            }
            for (DetalleHorario sesion : grupo.getSesiones()) {
                if (sesion.getAula() == null) {
                    throw new ReglaNegocioException(nombreGrupo + " no tiene aula asignada el " + sesion.getFranja());
                }
            }
        }
        periodo.setEstadoHorario(EstadoHorario.PUBLICADO);
        bitacoraService.registrar("PUBLICAR_HORARIO", periodo.getCodigo());
        notificarHorarioADocentes(periodo, grupos);
        return periodo;
    }

    // HU-40: avisa a los estudiantes N días antes de la apertura y del cierre de inscripción, una sola vez cada uno.
    @Override
    public void enviarAvisosInscripcion(LocalDate hoy) {
        int dias = parametrosAcademicosService.obtener().getDiasAnticipacionAviso();
        for (PeriodoAcademico periodo : periodoAcademicoRepository.findAll()) {
            if (periodo.estaCerrado()) {
                continue;
            }
            if (!periodo.isAvisoAperturaEnviado() && dentroDelAviso(hoy, periodo.getFechaAperturaInscripcion(), dias)) {
                avisarEstudiantes("Apertura de inscripción " + periodo.getCodigo(),
                        "La inscripción de materias del periodo " + periodo.getCodigo() + " abre el "
                                + periodo.getFechaAperturaInscripcion() + " y cierra el " + periodo.getFechaCierreInscripcion() + ".");
                periodo.setAvisoAperturaEnviado(true);
            }
            if (!periodo.isAvisoCierreEnviado() && dentroDelAviso(hoy, periodo.getFechaCierreInscripcion(), dias)) {
                avisarEstudiantes("Cierre de inscripción " + periodo.getCodigo(),
                        "La inscripción de materias del periodo " + periodo.getCodigo() + " cierra el "
                                + periodo.getFechaCierreInscripcion() + ".");
                periodo.setAvisoCierreEnviado(true);
            }
        }
    }

    private boolean dentroDelAviso(LocalDate hoy, LocalDate fecha, int diasAnticipacion) {
        return !hoy.isBefore(fecha.minusDays(diasAnticipacion)) && !hoy.isAfter(fecha);
    }

    private void avisarEstudiantes(String asunto, String mensaje) {
        estudianteRepository.findAll().forEach(estudiante -> notificacionService.notificar(estudiante, asunto, mensaje));
    }

    // HU-39: cada docente recibe su horario el mismo día de la publicación.
    private void notificarHorarioADocentes(PeriodoAcademico periodo, List<Grupo> grupos) {
        Map<Docente, List<Grupo>> gruposPorDocente = grupos.stream()
                .collect(Collectors.groupingBy(Grupo::getDocente, LinkedHashMap::new, Collectors.toList()));
        gruposPorDocente.forEach((docente, suyos) -> {
            String detalle = suyos.stream()
                    .map(grupo -> grupo.getMateria().getNombre() + " (grupo " + grupo.getCodigo() + "): "
                            + grupo.getSesiones().stream()
                            .map(sesion -> sesion.getFranja() + " en " + sesion.getAula().getCodigo())
                            .collect(Collectors.joining(", ")))
                    .collect(Collectors.joining("\n"));
            notificacionService.notificar(docente, "Horario publicado " + periodo.getCodigo(),
                    "Se publicó el horario del periodo " + periodo.getCodigo() + ". Tus clases:\n" + detalle);
        });
    }
}
