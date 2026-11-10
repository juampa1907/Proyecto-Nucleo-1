package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.dto.reporte.CargaDocente;
import co.edu.unbosque.collegeAdministrator.dto.reporte.CargaPorVinculacion;
import co.edu.unbosque.collegeAdministrator.dto.reporte.EstadoGrupos;
import co.edu.unbosque.collegeAdministrator.dto.reporte.IndicadorCarga;
import co.edu.unbosque.collegeAdministrator.dto.reporte.OcupacionAula;
import co.edu.unbosque.collegeAdministrator.dto.reporte.ReporteCargaDocente;
import co.edu.unbosque.collegeAdministrator.dto.reporte.ResumenGrupo;
import co.edu.unbosque.collegeAdministrator.dto.reporte.TablaReporte;
import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.DetalleHorario;
import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;
import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.entity.PresupuestoVinculacion;
import co.edu.unbosque.collegeAdministrator.entity.TipoVinculacion;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import co.edu.unbosque.collegeAdministrator.repository.DetalleHorarioRepository;
import co.edu.unbosque.collegeAdministrator.repository.InscripcionMateriaRepository;
import co.edu.unbosque.collegeAdministrator.service.api.AulaServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.DocenteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.GrupoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ParametrosAcademicosServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PresupuestoVinculacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ReporteServiceAPI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación de la generación de reportes.
 */
@Service
@Transactional
public class ReporteServiceImpl implements ReporteServiceAPI {

    /** Etiqueta para docentes sin tipo de vinculación. */
    private static final String SIN_VINCULACION = "Sin vinculación";

    /** El servicio de gestión de periodos académicos. */
    private final PeriodoAcademicoServiceAPI periodoAcademicoService;
    /** El servicio de parámetros académicos institucionales. */
    private final ParametrosAcademicosServiceAPI parametrosAcademicosService;
    /** El servicio de gestión de grupos y sesiones. */
    private final GrupoServiceAPI grupoService;
    /** El servicio de gestión de docentes. */
    private final DocenteServiceAPI docenteService;
    /** El servicio de gestión de aulas. */
    private final AulaServiceAPI aulaService;
    /** El servicio de presupuestos por vinculación. */
    private final PresupuestoVinculacionServiceAPI presupuestoVinculacionService;
    /** El repositorio de sesiones de horario. */
    private final DetalleHorarioRepository detalleHorarioRepository;
    /** El repositorio de inscripciones de materias. */
    private final InscripcionMateriaRepository inscripcionMateriaRepository;

    /**
     * Crea una instancia de {@code ReporteServiceImpl} con las dependencias requeridas.
     *
     * @param periodoAcademicoService el servicio de gestión de periodos académicos
     * @param parametrosAcademicosService el servicio de parámetros académicos institucionales
     * @param grupoService el servicio de gestión de grupos y sesiones
     * @param docenteService el servicio de gestión de docentes
     * @param aulaService el servicio de gestión de aulas
     * @param presupuestoVinculacionService el servicio de presupuestos por vinculación
     * @param detalleHorarioRepository el repositorio de sesiones de horario
     * @param inscripcionMateriaRepository el repositorio de inscripciones de materias
     */
    public ReporteServiceImpl(PeriodoAcademicoServiceAPI periodoAcademicoService,
                              ParametrosAcademicosServiceAPI parametrosAcademicosService,
                              GrupoServiceAPI grupoService,
                              DocenteServiceAPI docenteService,
                              AulaServiceAPI aulaService,
                              PresupuestoVinculacionServiceAPI presupuestoVinculacionService,
                              DetalleHorarioRepository detalleHorarioRepository,
                              InscripcionMateriaRepository inscripcionMateriaRepository) {
        this.periodoAcademicoService = periodoAcademicoService;
        this.parametrosAcademicosService = parametrosAcademicosService;
        this.grupoService = grupoService;
        this.docenteService = docenteService;
        this.aulaService = aulaService;
        this.presupuestoVinculacionService = presupuestoVinculacionService;
        this.detalleHorarioRepository = detalleHorarioRepository;
        this.inscripcionMateriaRepository = inscripcionMateriaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OcupacionAula> ocupacionAulas(Integer idPeriodo, Bloque bloque, DiaSemana dia, LocalTime desde, LocalTime hasta) {
        periodoAcademicoService.buscarPorId(idPeriodo);
        LocalTime inicioVentana = desde == null ? LocalTime.MIN : desde;
        LocalTime finVentana = hasta == null ? LocalTime.MAX : hasta;
        double horasDisponibles = horasDisponibles(parametrosAcademicosService.obtener(), dia, inicioVentana, finVentana);

        Map<Integer, List<DetalleHorario>> sesionesPorAula = detalleHorarioRepository.findByGrupo_Periodo_IdPeriodo(idPeriodo)
                .stream()
                .filter(sesion -> sesion.getAula() != null)
                .filter(sesion -> dia == null || sesion.getFranja().getDiaSemana() == dia)
                .filter(sesion -> minutosSolapados(sesion.getFranja().getHoraInicio(), sesion.getFranja().getHoraFin(),
                        inicioVentana, finVentana) > 0)
                .collect(Collectors.groupingBy(sesion -> sesion.getAula().getIdAula()));

        List<OcupacionAula> resultado = new ArrayList<>();
        for (Aula aula : aulaService.listarActivas()) {
            if (bloque != null && aula.getBloque() != bloque) {
                continue;
            }
            List<DetalleHorario> sesiones = sesionesPorAula.getOrDefault(aula.getIdAula(), List.of());
            double horasOcupadas = sesiones.stream()
                    .mapToLong(sesion -> minutosSolapados(sesion.getFranja().getHoraInicio(), sesion.getFranja().getHoraFin(),
                            inicioVentana, finVentana))
                    .sum() / 60.0;
            double porcentaje = horasDisponibles > 0 ? redondear(horasOcupadas * 100 / horasDisponibles) : 0;
            resultado.add(new OcupacionAula(aula.getIdAula(), aula.getCodigo(), aula.getBloque(), aula.getTipoEspacio(),
                    sesiones.size(), horasOcupadas, porcentaje));
        }
        resultado.sort(Comparator.comparing(OcupacionAula::bloque).thenComparing(OcupacionAula::codigo));
        return resultado;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReporteCargaDocente cargaDocente(Integer idPeriodo) {
        periodoAcademicoService.buscarPorId(idPeriodo);
        int umbral = parametrosAcademicosService.obtener().getUmbralSubutilizacionPorcentaje();

        List<CargaDocente> docentes = new ArrayList<>();
        for (Docente docente : docenteService.listar()) {
            TipoVinculacion tipo = docente.getTipoVinculacion();
            Integer maximas = tipo == null ? null : tipo.getHorasMaximasSemanales();
            double asignadas = docenteService.calcularHorasAsignadas(docente.getIdUsuario(), idPeriodo);
            docentes.add(new CargaDocente(docente.getIdUsuario(), docente.getUsername(),
                    tipo == null ? SIN_VINCULACION : tipo.getNombre(), maximas, asignadas,
                    indicador(asignadas, maximas, umbral)));
        }
        docentes.sort(Comparator.comparing(CargaDocente::tipoVinculacion).thenComparing(CargaDocente::username));

        Map<String, Integer> presupuestoPorTipo = presupuestoVinculacionService.listarPorPeriodo(idPeriodo).stream()
                .collect(Collectors.toMap(p -> p.getTipoVinculacion().getNombre(), PresupuestoVinculacion::getHorasPresupuestadas));
        Map<String, List<CargaDocente>> porTipo = docentes.stream()
                .collect(Collectors.groupingBy(CargaDocente::tipoVinculacion, LinkedHashMap::new, Collectors.toList()));
        List<CargaPorVinculacion> porVinculacion = new ArrayList<>();
        porTipo.forEach((tipo, suyos) -> {
            double asignadas = suyos.stream().mapToDouble(CargaDocente::horasAsignadas).sum();
            Integer presupuesto = presupuestoPorTipo.get(tipo);
            porVinculacion.add(new CargaPorVinculacion(tipo, suyos.size(), asignadas, presupuesto,
                    presupuesto == null ? null : presupuesto - asignadas));
        });
        return new ReporteCargaDocente(docentes, porVinculacion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EstadoGrupos estadoGrupos(Integer idPeriodo) {
        periodoAcademicoService.buscarPorId(idPeriodo);
        Integer minimo = parametrosAcademicosService.obtener().getMinimoEstudiantesGrupo();
        List<ResumenGrupo> abiertos = new ArrayList<>();
        List<ResumenGrupo> cerrados = new ArrayList<>();
        List<ResumenGrupo> enRiesgo = new ArrayList<>();
        for (Grupo grupo : grupoService.listarPorPeriodo(idPeriodo)) {
            long inscritos = inscripcionMateriaRepository.countByGrupo_IdGrupo(grupo.getIdGrupo());
            ResumenGrupo resumen = new ResumenGrupo(grupo.getIdGrupo(), grupo.getMateria().getNombre(), grupo.getCodigo(),
                    grupo.getDocente() == null ? null : grupo.getDocente().getUsername(), inscritos, grupo.getCupoMaximo());
            if (grupo.getEstado() == EstadoGrupo.CERRADO) {
                cerrados.add(resumen);
            } else {
                abiertos.add(resumen);
                if (minimo != null && inscritos < minimo) {
                    enRiesgo.add(resumen);
                }
            }
        }
        return new EstadoGrupos(abiertos, cerrados, enRiesgo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TablaReporte tablaOcupacionAulas(Integer idPeriodo, Bloque bloque, DiaSemana dia, LocalTime desde, LocalTime hasta) {
        List<List<String>> filas = ocupacionAulas(idPeriodo, bloque, dia, desde, hasta).stream()
                .map(o -> List.of(o.bloque().name(), o.codigo(), o.tipoEspacio().name(), String.valueOf(o.sesiones()),
                        formatear(o.horasOcupadas()), formatear(o.porcentajeOcupacion()) + " %"))
                .toList();
        String filtros = "Edificio: " + (bloque == null ? "todos" : bloque) + ", día: " + (dia == null ? "todos" : dia)
                + ", horas: " + (desde == null ? "inicio" : desde) + " a " + (hasta == null ? "fin" : hasta);
        return new TablaReporte("Ocupación de aulas — " + codigoPeriodo(idPeriodo) + " (" + filtros + ")",
                List.of("Edificio", "Aula", "Tipo", "Sesiones", "Horas ocupadas", "Ocupación"), filas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TablaReporte tablaCargaDocente(Integer idPeriodo) {
        List<List<String>> filas = new ArrayList<>();
        ReporteCargaDocente reporte = cargaDocente(idPeriodo);
        for (CargaDocente carga : reporte.docentes()) {
            filas.add(List.of(carga.tipoVinculacion(), carga.username(),
                    carga.horasMaximas() == null ? "-" : String.valueOf(carga.horasMaximas()),
                    formatear(carga.horasAsignadas()), carga.indicador().name()));
        }
        for (CargaPorVinculacion total : reporte.porVinculacion()) {
            filas.add(List.of(total.tipoVinculacion(), "TOTAL (" + total.docentes() + " docentes)",
                    total.horasPresupuestadas() == null ? "Sin presupuesto" : "Presupuesto: " + total.horasPresupuestadas(),
                    formatear(total.horasAsignadas()),
                    total.diferencia() == null ? "-" : "Diferencia: " + formatear(total.diferencia())));
        }
        return new TablaReporte("Carga horaria docente — " + codigoPeriodo(idPeriodo),
                List.of("Vinculación", "Docente", "Horas máximas", "Horas asignadas", "Indicador"), filas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TablaReporte tablaEstadoGrupos(Integer idPeriodo) {
        EstadoGrupos estado = estadoGrupos(idPeriodo);
        List<List<String>> filas = new ArrayList<>();
        agregarFilasGrupos(filas, estado.enRiesgo(), "EN RIESGO");
        agregarFilasGrupos(filas, estado.abiertos().stream().filter(g -> !estado.enRiesgo().contains(g)).toList(), "ABIERTO");
        agregarFilasGrupos(filas, estado.cerrados(), "CERRADO");
        return new TablaReporte("Estado de grupos — " + codigoPeriodo(idPeriodo),
                List.of("Estado", "Materia", "Grupo", "Docente", "Inscritos", "Cupo"), filas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TablaReporte horarioPeriodo(Integer idPeriodo) {
        List<List<String>> filas = new ArrayList<>();
        List<Grupo> grupos = grupoService.listarPorPeriodo(idPeriodo).stream()
                .sorted(Comparator.comparing((Grupo g) -> g.getMateria().getNombre()).thenComparing(Grupo::getCodigo))
                .toList();
        for (Grupo grupo : grupos) {
            String docente = grupo.getDocente() == null ? "Por asignar" : grupo.getDocente().getUsername();
            if (grupo.getSesiones().isEmpty()) {
                filas.add(List.of(grupo.getMateria().getNombre(), grupo.getCodigo(), docente, "Sin programar", "", "", "", ""));
            }
            grupo.getSesiones().stream()
                    .sorted(Comparator.comparing((DetalleHorario s) -> s.getFranja().getDiaSemana())
                            .thenComparing(s -> s.getFranja().getHoraInicio()))
                    .forEach(sesion -> {
                        FranjaHoraria franja = sesion.getFranja();
                        Aula aula = sesion.getAula();
                        filas.add(List.of(grupo.getMateria().getNombre(), grupo.getCodigo(), docente,
                                franja.getDiaSemana().name(), franja.getHoraInicio().toString(), franja.getHoraFin().toString(),
                                aula == null ? "Por asignar" : aula.getCodigo(), aula == null ? "" : aula.getBloque().name()));
                    });
        }
        return new TablaReporte("Horario — " + codigoPeriodo(idPeriodo),
                List.of("Materia", "Grupo", "Docente", "Día", "Inicio", "Fin", "Aula", "Edificio"), filas);
    }

    /**
     * Clasifica la carga de un docente.
     *
     * @param asignadas las horas asignadas
     * @param maximas las horas máximas, o {@code null} si no tiene vinculación
     * @param umbralPorcentaje el umbral de subutilización
     * @return el indicador de carga
     */
    private IndicadorCarga indicador(double asignadas, Integer maximas, int umbralPorcentaje) {
        if (maximas == null) {
            return IndicadorCarga.SIN_VINCULACION;
        }
        if (asignadas > maximas) {
            return IndicadorCarga.SOBREASIGNADO;
        }
        if (asignadas < maximas * umbralPorcentaje / 100.0) {
            return IndicadorCarga.SUBUTILIZADO;
        }
        return IndicadorCarga.ADECUADO;
    }

    /**
     * Calcula las horas hábiles de una ventana, sin contar el almuerzo.
     *
     * @param parametros los parámetros académicos
     * @param diaFiltro el día considerado, o {@code null} para todos
     * @param desde la hora inicial
     * @param hasta la hora final
     * @return las horas disponibles
     */
    private double horasDisponibles(ParametrosAcademicos parametros, DiaSemana diaFiltro, LocalTime desde, LocalTime hasta) {
        long minutos = 0;
        for (DiaSemana dia : DiaSemana.values()) {
            if (diaFiltro != null && dia != diaFiltro) {
                continue;
            }
            boolean sabado = dia == DiaSemana.SABADO;
            LocalTime inicioDia = sabado ? parametros.getHoraInicioSabado() : parametros.getHoraInicioSemana();
            LocalTime finDia = sabado ? parametros.getHoraFinSabado() : parametros.getHoraFinSemana();
            LocalTime inicio = inicioDia.isAfter(desde) ? inicioDia : desde;
            LocalTime fin = finDia.isBefore(hasta) ? finDia : hasta;
            if (!inicio.isBefore(fin)) {
                continue;
            }
            minutos += Duration.between(inicio, fin).toMinutes()
                    - minutosSolapados(inicio, fin, parametros.getInicioAlmuerzo(), parametros.getFinAlmuerzo());
        }
        return minutos / 60.0;
    }

    /**
     * Calcula los minutos comunes entre dos intervalos.
     *
     * @param inicioA el inicio del primer intervalo
     * @param finA el fin del primer intervalo
     * @param inicioB el inicio del segundo intervalo
     * @param finB el fin del segundo intervalo
     * @return los minutos solapados, o 0 si no se solapan
     */
    private long minutosSolapados(LocalTime inicioA, LocalTime finA, LocalTime inicioB, LocalTime finB) {
        LocalTime inicio = inicioA.isAfter(inicioB) ? inicioA : inicioB;
        LocalTime fin = finA.isBefore(finB) ? finA : finB;
        return inicio.isBefore(fin) ? Duration.between(inicio, fin).toMinutes() : 0;
    }

    /**
     * Agrega a la tabla una fila por cada grupo.
     *
     * @param filas las filas de la tabla
     * @param grupos los grupos
     * @param estado la etiqueta de estado de las filas
     */
    private void agregarFilasGrupos(List<List<String>> filas, List<ResumenGrupo> grupos, String estado) {
        for (ResumenGrupo grupo : grupos) {
            filas.add(List.of(estado, grupo.materia(), grupo.codigo(), grupo.docente() == null ? "Por asignar" : grupo.docente(),
                    String.valueOf(grupo.inscritos()), String.valueOf(grupo.cupoMaximo())));
        }
    }

    /**
     * Genera la etiqueta de un periodo.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return el texto con el código del periodo
     */
    private String codigoPeriodo(Integer idPeriodo) {
        PeriodoAcademico periodo = periodoAcademicoService.buscarPorId(idPeriodo);
        return "Periodo " + periodo.getCodigo();
    }

    /**
     * Redondea un valor a un decimal.
     *
     * @param valor el valor
     * @return el valor redondeado
     */
    private double redondear(double valor) {
        return Math.round(valor * 10) / 10.0;
    }

    /**
     * Da formato a un valor con un decimal.
     *
     * @param valor el valor
     * @return el valor con formato
     */
    private String formatear(double valor) {
        return String.format(Locale.ROOT, "%.1f", valor);
    }
}
