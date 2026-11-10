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

/**
 * Implementación de las operaciones sobre los periodos académicos.
 */
@Service
@Transactional
public class PeriodoAcademicoServiceImpl extends GenericServiceImpl<PeriodoAcademico, Integer>
        implements PeriodoAcademicoServiceAPI {

    /** El repositorio de periodos académicos. */
    private final PeriodoAcademicoRepository periodoAcademicoRepository;
    /** El repositorio de persistencia de grupos. */
    private final GrupoRepository grupoRepository;
    /** El repositorio de observaciones de asignación. */
    private final ObservacionAsignacionRepository observacionAsignacionRepository;
    /** El repositorio de persistencia de estudiantes. */
    private final EstudianteRepository estudianteRepository;
    /** El servicio de parámetros académicos institucionales. */
    private final ParametrosAcademicosServiceAPI parametrosAcademicosService;
    /** El servicio de notificaciones a usuarios. */
    private final NotificacionServiceAPI notificacionService;
    /** El servicio de auditoría donde se registran las acciones. */
    private final BitacoraServiceAPI bitacoraService;

    /**
     * Crea una instancia de {@code PeriodoAcademicoServiceImpl} con las dependencias requeridas.
     *
     * @param periodoAcademicoRepository el repositorio de periodos académicos
     * @param grupoRepository el repositorio de persistencia de grupos
     * @param observacionAsignacionRepository el repositorio de observaciones de asignación
     * @param estudianteRepository el repositorio de persistencia de estudiantes
     * @param parametrosAcademicosService el servicio de parámetros académicos institucionales
     * @param notificacionService el servicio de notificaciones a usuarios
     * @param bitacoraService el servicio de auditoría donde se registran las acciones
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<PeriodoAcademico, Integer> getRepository() {
        return periodoAcademicoRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Valida las fechas, impide códigos repetidos al crear y no permite modificar periodos cerrados.
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodoAcademico cerrar(Integer idPeriodo) {
        PeriodoAcademico periodo = buscarPorId(idPeriodo);
        periodo.setEstado(EstadoPeriodo.CERRADO);
        bitacoraService.registrar("CERRAR_PERIODO", periodo.getCodigo());
        return periodo;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * Indica si una fecha objetivo está dentro de la ventana de aviso.
     *
     * @param hoy la fecha de referencia
     * @param fecha la fecha objetivo
     * @param diasAnticipacion los días de anticipación
     * @return {@code true} si corresponde avisar
     */
    private boolean dentroDelAviso(LocalDate hoy, LocalDate fecha, int diasAnticipacion) {
        return !hoy.isBefore(fecha.minusDays(diasAnticipacion)) && !hoy.isAfter(fecha);
    }

    /**
     * Envía una notificación a todos los estudiantes.
     *
     * @param asunto el asunto
     * @param mensaje el contenido de la notificación
     */
    private void avisarEstudiantes(String asunto, String mensaje) {
        estudianteRepository.findAll().forEach(estudiante -> notificacionService.notificar(estudiante, asunto, mensaje));
    }

    /**
     * Envía a cada docente el detalle de sus clases publicadas.
     *
     * @param periodo el periodo publicado
     * @param grupos los grupos publicados
     */
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
