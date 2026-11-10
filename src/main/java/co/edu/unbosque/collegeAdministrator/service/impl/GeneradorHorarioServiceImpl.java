package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.dto.ResultadoGeneracion;
import co.edu.unbosque.collegeAdministrator.dto.ResultadoGeneracion.GrupoNoProgramado;
import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;
import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import co.edu.unbosque.collegeAdministrator.repository.GrupoRepository;
import co.edu.unbosque.collegeAdministrator.service.api.AulaServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.DocenteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.GeneradorHorarioServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.GrupoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ParametrosAcademicosServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.VersionHorarioServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Implementación de la generación automática de la propuesta de horario. Atiende primero los grupos con más
 * requisitos de aula, mayor cupo y menos docentes habilitados.
 */
@Service
@Transactional
public class GeneradorHorarioServiceImpl implements GeneradorHorarioServiceAPI {

    /** Separación en minutos entre los inicios de las franjas candidatas. */
    private static final int MINUTOS_ENTRE_INICIOS = 60;
    /** Número máximo de aulas evaluadas por franja. */
    private static final int MAX_AULAS_POR_FRANJA = 15;

    /** El repositorio de persistencia de grupos. */
    private final GrupoRepository grupoRepository;
    /** El servicio de gestión de grupos y sesiones. */
    private final GrupoServiceAPI grupoService;
    /** El servicio de gestión de docentes. */
    private final DocenteServiceAPI docenteService;
    /** El servicio de gestión de aulas. */
    private final AulaServiceAPI aulaService;
    /** El servicio de gestión de periodos académicos. */
    private final PeriodoAcademicoServiceAPI periodoAcademicoService;
    /** El servicio de parámetros académicos institucionales. */
    private final ParametrosAcademicosServiceAPI parametrosAcademicosService;
    /** El servicio de versiones del horario. */
    private final VersionHorarioServiceAPI versionHorarioService;
    /** El servicio de auditoría donde se registran las acciones. */
    private final BitacoraServiceAPI bitacoraService;

    /**
     * Crea una instancia de {@code GeneradorHorarioServiceImpl} con las dependencias requeridas.
     *
     * @param grupoRepository el repositorio de persistencia de grupos
     * @param grupoService el servicio de gestión de grupos y sesiones
     * @param docenteService el servicio de gestión de docentes
     * @param aulaService el servicio de gestión de aulas
     * @param periodoAcademicoService el servicio de gestión de periodos académicos
     * @param parametrosAcademicosService el servicio de parámetros académicos institucionales
     * @param versionHorarioService el servicio de versiones del horario
     * @param bitacoraService el servicio de auditoría donde se registran las acciones
     */
    public GeneradorHorarioServiceImpl(GrupoRepository grupoRepository,
                                       GrupoServiceAPI grupoService,
                                       DocenteServiceAPI docenteService,
                                       AulaServiceAPI aulaService,
                                       PeriodoAcademicoServiceAPI periodoAcademicoService,
                                       ParametrosAcademicosServiceAPI parametrosAcademicosService,
                                       VersionHorarioServiceAPI versionHorarioService,
                                       BitacoraServiceAPI bitacoraService) {
        this.grupoRepository = grupoRepository;
        this.grupoService = grupoService;
        this.docenteService = docenteService;
        this.aulaService = aulaService;
        this.periodoAcademicoService = periodoAcademicoService;
        this.parametrosAcademicosService = parametrosAcademicosService;
        this.versionHorarioService = versionHorarioService;
        this.bitacoraService = bitacoraService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultadoGeneracion generarPropuesta(Integer idPeriodo) {
        PeriodoAcademico periodo = periodoAcademicoService.buscarPorId(idPeriodo);
        if (periodo.estaCerrado() || periodo.horarioPublicado()) {
            throw new ReglaNegocioException("Solo se genera propuesta para un periodo abierto con el horario en borrador");
        }
        ParametrosAcademicos parametros = parametrosAcademicosService.obtener();
        versionHorarioService.guardarVersion(idPeriodo, "Antes de generar la propuesta automática");

        List<FranjaHoraria> franjas = generarFranjasCandidatas(parametros);
        Map<Integer, Integer> habilitadosPorMateria = new HashMap<>();
        List<Grupo> pendientes = grupoRepository.findByPeriodo_IdPeriodoAndEstado(idPeriodo, EstadoGrupo.ABIERTO).stream()
                .filter(grupo -> sesionesFaltantes(grupo, parametros) > 0)
                .sorted(Comparator
                        .comparingInt((Grupo g) -> -g.getMateria().getRequisitosAula().size())
                        .thenComparingInt(g -> -g.getCupoMaximo())
                        .thenComparingInt(g -> habilitadosPorMateria.computeIfAbsent(g.getMateria().getIdMateria(),
                                id -> docenteService.listarHabilitadosParaMateria(id).size())))
                .toList();

        List<String> programados = new ArrayList<>();
        List<GrupoNoProgramado> noProgramados = new ArrayList<>();
        for (Grupo grupo : pendientes) {
            String motivo = programarGrupo(grupo, franjas, parametros);
            if (motivo == null) {
                programados.add(describir(grupo));
            } else {
                noProgramados.add(new GrupoNoProgramado(grupo.getIdGrupo(), describir(grupo), motivo));
            }
        }
        bitacoraService.registrar("GENERAR_PROPUESTA", "Periodo " + periodo.getCodigo() + ": " + programados.size()
                + " grupos programados, " + noProgramados.size() + " sin completar");
        return new ResultadoGeneracion(programados, noProgramados);
    }


    /**
     * Asigna docente si hace falta y programa las sesiones faltantes de un grupo.
     *
     * @param grupo el grupo
     * @param franjas las franjas candidatas
     * @param parametros los parámetros académicos
     * @return {@code null} si se completó o el motivo por el que no se pudo
     */
    private String programarGrupo(Grupo grupo, List<FranjaHoraria> franjas, ParametrosAcademicos parametros) {
        Materia materia = grupo.getMateria();
        int faltantes = sesionesFaltantes(grupo, parametros);

        if (grupo.getDocente() == null) {
            String motivoDocente = asignarMejorDocente(grupo, faltantes * parametros.getDuracionSesionMinutos() / 60.0);
            if (motivoDocente != null) {
                return motivoDocente;
            }
        } else if (grupo.getDocente().getTipoVinculacion() == null) {
            return "El docente asignado no tiene tipo de vinculación";
        }

        List<Aula> aulas = aulaService.sugerirAulas(grupo.getCupoMaximo(), materia.getRequisitosAula(), null).stream()
                .sorted(Comparator.comparingInt(Aula::getCapacidad))
                .limit(MAX_AULAS_POR_FRANJA)
                .toList();
        if (aulas.isEmpty()) {
            return "No hay aulas activas con capacidad para " + grupo.getCupoMaximo() + " estudiantes"
                    + (materia.getRequisitosAula().isEmpty() ? "" : " y con " + materia.getRequisitosAula());
        }

        Set<DiaSemana> diasUsados = EnumSet.noneOf(DiaSemana.class);
        grupo.getSesiones().forEach(sesion -> diasUsados.add(sesion.getFranja().getDiaSemana()));
        for (int i = 0; i < faltantes; i++) {
            if (!programarSesion(grupo, franjas, aulas, diasUsados)) {
                return "No hay una franja en la que coincidan el docente y un aula compatible libres ("
                        + i + " de " + faltantes + " sesiones programadas)";
            }
        }
        return null;
    }

    /**
     * Asigna al docente habilitado con más horas disponibles que pueda asumir el grupo.
     *
     * @param grupo el grupo
     * @param horasNecesarias las horas que requiere el grupo
     * @return {@code null} si se asignó o el motivo por el que no se pudo
     */
    private String asignarMejorDocente(Grupo grupo, double horasNecesarias) {
        List<Docente> candidatos = docenteService.listarHabilitadosParaMateria(grupo.getMateria().getIdMateria()).stream()
                .filter(docente -> docente.getTipoVinculacion() != null)
                .toList();
        if (candidatos.isEmpty()) {
            return "No hay docentes habilitados con tipo de vinculación para " + grupo.getMateria().getNombre();
        }
        Integer idPeriodo = grupo.getPeriodo().getIdPeriodo();
        Map<Integer, Double> disponibles = new HashMap<>();
        candidatos.forEach(d -> disponibles.put(d.getIdUsuario(), docenteService.calcularHorasDisponibles(d.getIdUsuario(), idPeriodo)));
        for (Docente docente : candidatos.stream()
                .sorted(Comparator.comparingDouble((Docente d) -> disponibles.get(d.getIdUsuario())).reversed())
                .toList()) {
            if (disponibles.get(docente.getIdUsuario()) >= horasNecesarias
                    && grupoService.evaluarDocente(grupo.getIdGrupo(), docente.getIdUsuario()) == null) {
                grupoService.asignarDocenteGenerado(grupo.getIdGrupo(), docente.getIdUsuario());
                return null;
            }
        }
        return "Ningún docente habilitado tiene " + horasNecesarias + " horas disponibles sin cruces en este periodo";
    }

    /**
     * Busca la primera franja y aula válidas y crea una sesión.
     *
     * @param grupo el grupo
     * @param franjas las franjas candidatas
     * @param aulas las aulas candidatas
     * @param diasUsados los días ya usados por el grupo; se actualiza al programar
     * @return {@code true} si se programó la sesión
     */
    private boolean programarSesion(Grupo grupo, List<FranjaHoraria> franjas, List<Aula> aulas, Set<DiaSemana> diasUsados) {
        List<FranjaHoraria> ordenadas = franjas.stream()
                .sorted(Comparator.comparingInt((FranjaHoraria f) -> prioridadDia(f.getDiaSemana(), diasUsados))
                        .thenComparing(FranjaHoraria::getHoraInicio))
                .toList();
        for (FranjaHoraria franja : ordenadas) {
            // Primero sin aula: descarta rápido las franjas que fallan por el docente o por los parámetros.
            if (grupoService.evaluarSesion(grupo.getIdGrupo(), franja, null) != null) {
                continue;
            }
            for (Aula aula : aulas) {
                if (grupoService.evaluarSesion(grupo.getIdGrupo(), franja, aula.getIdAula()) == null) {
                    grupoService.agregarSesionGenerada(grupo.getIdGrupo(), franja, aula.getIdAula());
                    diasUsados.add(franja.getDiaSemana());
                    return true;
                }
            }
        }
        return false;
    }

    // 0 = día libre y no vecino a uno usado; 1 = día libre pero consecutivo; 2 = día ya usado por el grupo.
    /**
     * Calcula la prioridad de un día para distribuir las sesiones.
     *
     * @param dia el día evaluado
     * @param diasUsados los días ya usados por el grupo
     * @return 0 si el día está libre y no es vecino de uno usado, 1 si está libre pero es consecutivo y 2 si
     *         ya está usado
     */
    private int prioridadDia(DiaSemana dia, Set<DiaSemana> diasUsados) {
        if (diasUsados.contains(dia)) {
            return 2;
        }
        boolean consecutivo = diasUsados.stream().anyMatch(usado -> Math.abs(usado.ordinal() - dia.ordinal()) == 1);
        return consecutivo ? 1 : 0;
    }

    /**
     * Genera las franjas permitidas de todos los días según los parámetros.
     *
     * @param parametros los parámetros académicos
     * @return las franjas candidatas
     */
    private List<FranjaHoraria> generarFranjasCandidatas(ParametrosAcademicos parametros) {
        List<FranjaHoraria> franjas = new ArrayList<>();
        int duracion = parametros.getDuracionSesionMinutos();
        for (DiaSemana dia : DiaSemana.values()) {
            boolean sabado = dia == DiaSemana.SABADO;
            LocalTime inicio = sabado ? parametros.getHoraInicioSabado() : parametros.getHoraInicioSemana();
            LocalTime fin = sabado ? parametros.getHoraFinSabado() : parametros.getHoraFinSemana();
            // Se avanza en minutos desde el inicio del día para no depender de LocalTime, que da la vuelta a medianoche.
            int minutosDelDia = (int) Duration.between(inicio, fin).toMinutes();
            for (int desplazamiento = 0; desplazamiento + duracion <= minutosDelDia; desplazamiento += MINUTOS_ENTRE_INICIOS) {
                LocalTime hora = inicio.plusMinutes(desplazamiento);
                FranjaHoraria franja = new FranjaHoraria(dia, hora, hora.plusMinutes(duracion));
                if (parametros.franjaPermitida(franja)) {
                    franjas.add(franja);
                }
            }
        }
        return franjas;
    }

    /**
     * Calcula cuántas sesiones le faltan a un grupo según las horas de su materia y el máximo semanal.
     *
     * @param grupo el grupo
     * @param parametros los parámetros académicos
     * @return las sesiones faltantes, nunca negativo
     */
    private int sesionesFaltantes(Grupo grupo, ParametrosAcademicos parametros) {
        int requeridas = (int) Math.ceil(grupo.getMateria().getHorasSemanales() * 60.0 / parametros.getDuracionSesionMinutos());
        requeridas = Math.min(requeridas, parametros.getMaxSesionesSemanales());
        return Math.max(0, requeridas - grupo.getSesiones().size());
    }

    /**
     * Describe un grupo con su materia y código.
     *
     * @param grupo el grupo
     * @return la descripción del grupo
     */
    private String describir(Grupo grupo) {
        return grupo.getMateria().getNombre() + " (grupo " + grupo.getCodigo() + ")";
    }
}
