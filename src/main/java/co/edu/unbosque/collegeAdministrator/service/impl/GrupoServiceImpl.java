package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.DetalleHorario;
import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;
import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import co.edu.unbosque.collegeAdministrator.repository.DetalleHorarioRepository;
import co.edu.unbosque.collegeAdministrator.repository.GrupoRepository;
import co.edu.unbosque.collegeAdministrator.repository.InscripcionMateriaRepository;
import co.edu.unbosque.collegeAdministrator.service.api.AulaServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.DocenteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.GrupoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.MateriaServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.NotificacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ParametrosAcademicosServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.VersionHorarioServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Implementación de las operaciones sobre grupos y de las reglas de programación horaria.
 */
@Service
@Transactional
public class GrupoServiceImpl extends GenericServiceImpl<Grupo, Integer> implements GrupoServiceAPI {

    /** El repositorio de persistencia de grupos. */
    private final GrupoRepository grupoRepository;
    /** El repositorio de sesiones de horario. */
    private final DetalleHorarioRepository detalleHorarioRepository;
    /** El repositorio de inscripciones de materias. */
    private final InscripcionMateriaRepository inscripcionMateriaRepository;
    /** El servicio de gestión de materias. */
    private final MateriaServiceAPI materiaService;
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
    /** El servicio de notificaciones a usuarios. */
    private final NotificacionServiceAPI notificacionService;

    /**
     * Crea una instancia de {@code GrupoServiceImpl} con las dependencias requeridas.
     *
     * @param grupoRepository el repositorio de persistencia de grupos
     * @param detalleHorarioRepository el repositorio de sesiones de horario
     * @param inscripcionMateriaRepository el repositorio de inscripciones de materias
     * @param materiaService el servicio de gestión de materias
     * @param docenteService el servicio de gestión de docentes
     * @param aulaService el servicio de gestión de aulas
     * @param periodoAcademicoService el servicio de gestión de periodos académicos
     * @param parametrosAcademicosService el servicio de parámetros académicos institucionales
     * @param versionHorarioService el servicio de versiones del horario
     * @param bitacoraService el servicio de auditoría donde se registran las acciones
     * @param notificacionService el servicio de notificaciones a usuarios
     */
    public GrupoServiceImpl(GrupoRepository grupoRepository,
                            DetalleHorarioRepository detalleHorarioRepository,
                            InscripcionMateriaRepository inscripcionMateriaRepository,
                            MateriaServiceAPI materiaService,
                            DocenteServiceAPI docenteService,
                            AulaServiceAPI aulaService,
                            PeriodoAcademicoServiceAPI periodoAcademicoService,
                            ParametrosAcademicosServiceAPI parametrosAcademicosService,
                            VersionHorarioServiceAPI versionHorarioService,
                            BitacoraServiceAPI bitacoraService,
                            NotificacionServiceAPI notificacionService) {
        this.grupoRepository = grupoRepository;
        this.detalleHorarioRepository = detalleHorarioRepository;
        this.inscripcionMateriaRepository = inscripcionMateriaRepository;
        this.materiaService = materiaService;
        this.docenteService = docenteService;
        this.aulaService = aulaService;
        this.periodoAcademicoService = periodoAcademicoService;
        this.parametrosAcademicosService = parametrosAcademicosService;
        this.versionHorarioService = versionHorarioService;
        this.bitacoraService = bitacoraService;
        this.notificacionService = notificacionService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<Grupo, Integer> getRepository() {
        return grupoRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grupo crearGrupo(Integer idPeriodo, Integer idMateria, String codigo, Integer cupoMaximo) {
        if (codigo == null || codigo.isBlank()) {
            throw new ReglaNegocioException("El código del grupo es obligatorio");
        }
        PeriodoAcademico periodo = periodoAcademicoService.buscarPorId(idPeriodo);
        validarHorarioEditable(periodo);
        Materia materia = materiaService.buscarPorId(idMateria);
        if (grupoRepository.existsByPeriodo_IdPeriodoAndMateria_IdMateriaAndCodigoIgnoreCase(idPeriodo, idMateria, codigo)) {
            throw new ReglaNegocioException("Ya existe el grupo " + codigo + " de " + materia.getNombre()
                    + " en el periodo " + periodo.getCodigo());
        }
        int cupoPermitido = parametrosAcademicosService.obtener().calcularCupoMaximoPermitido();
        int cupo = cupoMaximo == null ? cupoPermitido : cupoMaximo;
        if (cupo <= 0) {
            throw new ReglaNegocioException("El cupo máximo debe ser mayor a 0");
        }
        if (cupo > cupoPermitido) {
            throw new ReglaNegocioException("El cupo " + cupo + " supera el máximo permitido de " + cupoPermitido);
        }
        return grupoRepository.save(new Grupo(codigo, materia, periodo, cupo));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Grupo> listarPorPeriodo(Integer idPeriodo) {
        return grupoRepository.findByPeriodo_IdPeriodo(idPeriodo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Grupo> listarOferta(Integer idPeriodo, Integer idMateria) {
        return grupoRepository.findByPeriodo_IdPeriodoAndMateria_IdMateriaAndEstado(idPeriodo, idMateria, EstadoGrupo.ABIERTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Grupo> listarPorDocente(Integer idDocente, Integer idPeriodo) {
        return grupoRepository.findByDocente_IdUsuarioAndPeriodo_IdPeriodo(idDocente, idPeriodo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long calcularCuposDisponibles(Integer idGrupo) {
        Grupo grupo = buscarPorId(idGrupo);
        return grupo.getCupoMaximo() - inscripcionMateriaRepository.countByGrupo_IdGrupo(idGrupo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grupo asignarDocente(Integer idGrupo, Integer idDocente) {
        Grupo grupo = buscarPorId(idGrupo);
        validarHorarioEditable(grupo.getPeriodo());
        validarGrupoAbierto(grupo);
        if (grupo.getDocente() != null && grupo.getDocente().getIdUsuario().equals(idDocente)) {
            return grupo;
        }
        Docente docente = docenteService.buscarPorId(idDocente);
        validarDocente(docente, grupo, franjasDe(grupo), grupo.calcularHorasSemanales());
        versionHorarioService.guardarVersion(grupo.getPeriodo().getIdPeriodo(),
                "Antes de asignar docente a " + describir(grupo));
        grupo.setDocente(docente);
        bitacoraService.registrar("ASIGNAR_DOCENTE", docente.getUsername() + " → " + describir(grupo));
        return grupo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grupo agregarSesion(Integer idGrupo, FranjaHoraria franja, Integer idAula, boolean usoExcepcional) {
        Grupo grupo = buscarPorId(idGrupo);
        validarHorarioEditable(grupo.getPeriodo());
        validarSesion(grupo, franja, idAula, usoExcepcional);
        versionHorarioService.guardarVersion(grupo.getPeriodo().getIdPeriodo(), "Antes de agregar sesión a " + describir(grupo));
        DetalleHorario sesion = crearSesion(grupo, franja, idAula, usoExcepcional);
        bitacoraService.registrar(usoExcepcional ? "AGREGAR_SESION_EXCEPCIONAL" : "AGREGAR_SESION",
                describir(grupo) + " " + franja + aulaTexto(sesion.getAula()));
        return grupo;
    }

    // HU-25: todo ocurre en la misma transacción; si la nueva posición no es válida se revierte el retiro.
    /**
     * {@inheritDoc}
     */
    @Override
    public Grupo moverSesion(Integer idGrupo, Integer idSesion, FranjaHoraria franja, Integer idAula, boolean usoExcepcional) {
        Grupo grupo = buscarPorId(idGrupo);
        validarHorarioEditable(grupo.getPeriodo());
        DetalleHorario anterior = buscarSesion(grupo, idSesion);
        String origen = anterior.getFranja() + aulaTexto(anterior.getAula());
        versionHorarioService.guardarVersion(grupo.getPeriodo().getIdPeriodo(), "Antes de mover sesión de " + describir(grupo));
        grupo.quitarSesion(anterior);
        validarSesion(grupo, franja, idAula, usoExcepcional);
        DetalleHorario nueva = crearSesion(grupo, franja, idAula, usoExcepcional);
        bitacoraService.registrar("MOVER_SESION", describir(grupo) + ": " + origen + " → " + franja + aulaTexto(nueva.getAula()));
        return grupo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grupo quitarSesion(Integer idGrupo, Integer idSesion) {
        Grupo grupo = buscarPorId(idGrupo);
        validarHorarioEditable(grupo.getPeriodo());
        DetalleHorario sesion = buscarSesion(grupo, idSesion);
        versionHorarioService.guardarVersion(grupo.getPeriodo().getIdPeriodo(), "Antes de quitar sesión de " + describir(grupo));
        grupo.quitarSesion(sesion);
        bitacoraService.registrar("QUITAR_SESION", describir(grupo) + " " + sesion.getFranja());
        return grupo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> advertenciasDiasConsecutivos(Integer idGrupo, FranjaHoraria franjaNueva) {
        Grupo grupo = buscarPorId(idGrupo);
        Set<DiaSemana> dias = new TreeSet<>();
        grupo.getSesiones().forEach(sesion -> dias.add(sesion.getFranja().getDiaSemana()));
        if (franjaNueva != null) {
            dias.add(franjaNueva.getDiaSemana());
        }
        List<String> advertencias = new ArrayList<>();
        DiaSemana anterior = null;
        for (DiaSemana dia : dias) {
            if (anterior != null && dia.ordinal() - anterior.ordinal() == 1) {
                advertencias.add("Las sesiones del " + anterior + " y el " + dia + " quedan en días consecutivos");
            }
            anterior = dia;
        }
        return advertencias;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Aula> sugerirAulas(Integer idGrupo) {
        Grupo grupo = buscarPorId(idGrupo);
        return aulaService.sugerirAulas(grupo.getCupoMaximo(), grupo.getMateria().getRequisitosAula(), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grupo cerrarGrupo(Integer idGrupo) {
        Grupo grupo = buscarPorId(idGrupo);
        validarPeriodoNoCerrado(grupo.getPeriodo());
        grupo.setEstado(EstadoGrupo.CERRADO);
        bitacoraService.registrar("CERRAR_GRUPO", describir(grupo));
        notificarCierre(grupo);
        return grupo;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Grupo> listarGruposEnRiesgo(Integer idPeriodo) {
        Integer minimo = parametrosAcademicosService.obtener().getMinimoEstudiantesGrupo();
        if (minimo == null) {
            throw new ReglaNegocioException("No se ha configurado el mínimo de estudiantes por grupo");
        }
        return grupoRepository.findByPeriodo_IdPeriodoAndEstado(idPeriodo, EstadoGrupo.ABIERTO).stream()
                .filter(grupo -> inscripcionMateriaRepository.countByGrupo_IdGrupo(grupo.getIdGrupo()) < minimo)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Grupo> cerrarGruposBajoMinimo(Integer idPeriodo) {
        PeriodoAcademico periodo = periodoAcademicoService.buscarPorId(idPeriodo);
        validarPeriodoNoCerrado(periodo);
        if (!LocalDate.now().isAfter(periodo.getFechaCierreInscripcion())) {
            throw new ReglaNegocioException("Los grupos solo se cierran por bajo número de estudiantes después del "
                    + periodo.getFechaCierreInscripcion());
        }
        List<Grupo> enRiesgo = listarGruposEnRiesgo(idPeriodo);
        for (Grupo grupo : enRiesgo) {
            grupo.setEstado(EstadoGrupo.CERRADO);
            bitacoraService.registrar("CERRAR_GRUPO_BAJO_MINIMO", describir(grupo));
            notificarCierre(grupo);
        }
        return enRiesgo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public String evaluarSesion(Integer idGrupo, FranjaHoraria franja, Integer idAula) {
        Grupo grupo = buscarPorId(idGrupo);
        try {
            validarHorarioEditable(grupo.getPeriodo());
            validarSesion(grupo, franja, idAula, false);
            return null;
        } catch (ReglaNegocioException e) {
            return e.getMessage();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public String evaluarDocente(Integer idGrupo, Integer idDocente) {
        Grupo grupo = buscarPorId(idGrupo);
        Docente docente = docenteService.buscarPorId(idDocente);
        if (docente.getTipoVinculacion() == null) {
            return "El docente " + docente.getUsername() + " no tiene tipo de vinculación";
        }
        try {
            validarDocente(docente, grupo, franjasDe(grupo), grupo.calcularHorasSemanales());
            return null;
        } catch (ReglaNegocioException e) {
            return e.getMessage();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grupo agregarSesionGenerada(Integer idGrupo, FranjaHoraria franja, Integer idAula) {
        Grupo grupo = buscarPorId(idGrupo);
        validarHorarioEditable(grupo.getPeriodo());
        validarSesion(grupo, franja, idAula, false);
        crearSesion(grupo, franja, idAula, false);
        return grupo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Grupo asignarDocenteGenerado(Integer idGrupo, Integer idDocente) {
        Grupo grupo = buscarPorId(idGrupo);
        validarHorarioEditable(grupo.getPeriodo());
        Docente docente = docenteService.buscarPorId(idDocente);
        validarDocente(docente, grupo, franjasDe(grupo), grupo.calcularHorasSemanales());
        grupo.setDocente(docente);
        return grupo;
    }

    /**
     * Crea una sesión con su propia copia de la franja, porque un objeto embebible no debe compartirse entre
     * entidades.
     *
     * @param grupo el grupo
     * @param franja la franja de la sesión
     * @param idAula el identificador del aula, o {@code null}
     * @param usoExcepcional {@code true} si el aula es de uso excepcional
     * @return la sesión creada
     */
    private DetalleHorario crearSesion(Grupo grupo, FranjaHoraria franja, Integer idAula, boolean usoExcepcional) {
        Aula aula = idAula == null ? null : aulaService.buscarPorId(idAula);
        // Un @Embeddable no debe compartirse entre entidades: cada sesión lleva su propia copia de la franja.
        FranjaHoraria copia = new FranjaHoraria(franja.getDiaSemana(), franja.getHoraInicio(), franja.getHoraFin());
        DetalleHorario sesion = new DetalleHorario(copia, aula, usoExcepcional);
        grupo.agregarSesion(sesion);
        return sesion;
    }

    /**
     * Aplica las reglas de programación de una sesión: grupo abierto, parámetros institucionales, cruces,
     * aula y docente.
     *
     * @param grupo el grupo
     * @param franja la franja evaluada
     * @param idAula el identificador del aula, o {@code null}
     * @param usoExcepcional {@code true} si el aula es de uso excepcional
     * @throws ReglaNegocioException si incumple alguna regla
     */
    private void validarSesion(Grupo grupo, FranjaHoraria franja, Integer idAula, boolean usoExcepcional) {
        if (franja == null) {
            throw new ReglaNegocioException("La franja horaria es obligatoria");
        }
        validarGrupoAbierto(grupo);
        validarFranjaConParametros(grupo, franja);
        for (DetalleHorario sesion : grupo.getSesiones()) {
            if (sesion.getFranja().seCruzaCon(franja)) {
                throw new ReglaNegocioException("La franja " + franja
                        + " se cruza con otra sesión del mismo grupo (" + sesion.getFranja() + ")");
            }
        }
        if (idAula != null) {
            validarAula(aulaService.buscarPorId(idAula), grupo, franja, usoExcepcional);
        }
        if (grupo.getDocente() != null) {
            validarDocente(grupo.getDocente(), grupo, List.of(franja), franja.calcularHoras());
        }
    }

    /**
     * Verifica que un periodo no esté cerrado.
     *
     * @param periodo el periodo
     * @throws ReglaNegocioException si está cerrado
     */
    private void validarPeriodoNoCerrado(PeriodoAcademico periodo) {
        if (periodo.estaCerrado()) {
            throw new ReglaNegocioException("El periodo " + periodo.getCodigo() + " está cerrado y es de solo lectura");
        }
    }

    /**
     * Verifica que un periodo no esté cerrado ni tenga el horario publicado.
     *
     * @param periodo el periodo
     * @throws ReglaNegocioException si no es editable
     */
    private void validarHorarioEditable(PeriodoAcademico periodo) {
        validarPeriodoNoCerrado(periodo);
        if (periodo.horarioPublicado()) {
            throw new ReglaNegocioException("El horario del periodo " + periodo.getCodigo()
                    + " ya fue publicado y no se puede modificar");
        }
    }

    /**
     * Verifica que un grupo no esté cerrado.
     *
     * @param grupo el grupo
     * @throws ReglaNegocioException si está cerrado
     */
    private void validarGrupoAbierto(Grupo grupo) {
        if (grupo.getEstado() == EstadoGrupo.CERRADO) {
            throw new ReglaNegocioException("El grupo " + grupo.getCodigo() + " está cerrado");
        }
    }

    /**
     * Valida la duración, la jornada, el máximo de sesiones y las horas de la materia.
     *
     * @param grupo el grupo
     * @param franja la franja evaluada
     * @throws ReglaNegocioException si incumple algún parámetro
     */
    private void validarFranjaConParametros(Grupo grupo, FranjaHoraria franja) {
        ParametrosAcademicos parametros = parametrosAcademicosService.obtener();
        if (franja.calcularMinutos() != parametros.getDuracionSesionMinutos()) {
            throw new ReglaNegocioException("Cada sesión debe durar " + parametros.getDuracionSesionMinutos() + " minutos");
        }
        if (!parametros.franjaPermitida(franja)) {
            throw new ReglaNegocioException("La franja " + franja
                    + " está fuera del horario institucional o cruza la franja de almuerzo");
        }
        if (grupo.getSesiones().size() >= parametros.getMaxSesionesSemanales()) {
            throw new ReglaNegocioException("El grupo ya tiene el máximo de " + parametros.getMaxSesionesSemanales()
                    + " sesiones semanales");
        }
        Materia materia = grupo.getMateria();
        if (grupo.calcularHorasSemanales() + franja.calcularHoras() > materia.getHorasSemanales()) {
            throw new ReglaNegocioException(materia.getNombre() + " tiene " + materia.getHorasSemanales()
                    + " horas semanales y esta sesión las superaría");
        }
    }


    /**
     * Valida que un aula esté activa, sea compatible con el grupo y esté libre.
     *
     * @param aula el aula
     * @param grupo el grupo
     * @param franja la franja evaluada
     * @param usoExcepcional {@code true} si el aula es de uso excepcional
     * @throws ReglaNegocioException si el aula no es válida
     */
    private void validarAula(Aula aula, Grupo grupo, FranjaHoraria franja, boolean usoExcepcional) {
        if (!aula.isActiva()) {
            throw new ReglaNegocioException("El aula " + aula.getCodigo() + " está inactiva");
        }
        if (aula.getTipoEspacio() == TipoEspacio.AUDITORIO && !usoExcepcional) {
            throw new ReglaNegocioException("El auditorio " + aula.getCodigo()
                    + " solo puede asignarse marcándolo como uso excepcional");
        }
        if (aula.getCapacidad() < grupo.getCupoMaximo()) {
            throw new ReglaNegocioException("El aula " + aula.getCodigo() + " tiene capacidad " + aula.getCapacidad()
                    + " y el grupo tiene cupo de " + grupo.getCupoMaximo());
        }
        Set<CaracteristicaAula> faltantes = grupo.getMateria().getRequisitosAula().stream()
                .filter(requisito -> !aula.tieneCaracteristica(requisito))
                .collect(Collectors.toSet());
        if (!faltantes.isEmpty()) {
            throw new ReglaNegocioException("El aula " + aula.getCodigo() + " no cumple los requisitos de "
                    + grupo.getMateria().getNombre() + ": falta " + faltantes);
        }
        detalleHorarioRepository.buscarCrucesDeAula(aula.getIdAula(), grupo.getPeriodo().getIdPeriodo(),
                        franja.getDiaSemana(), franja.getHoraInicio(), franja.getHoraFin())
                .stream()
                .findFirst()
                .ifPresent(ocupada -> {
                    throw new ReglaNegocioException("El aula " + aula.getCodigo() + " ya está ocupada por "
                            + describir(ocupada.getGrupo()) + " el " + ocupada.getFranja());
                });
    }

    /**
     * Valida la habilitación, las restricciones, los cruces y las horas disponibles de un docente.
     *
     * @param docente el docente
     * @param grupo el grupo
     * @param franjas las franjas evaluadas
     * @param horasNuevas las horas que se le agregarían
     * @throws ReglaNegocioException si el docente no cumple alguna regla
     */
    private void validarDocente(Docente docente, Grupo grupo, List<FranjaHoraria> franjas, double horasNuevas) {
        boolean habilitado = docente.getMateriasHabilitadas().stream()
                .anyMatch(m -> m.getIdMateria().equals(grupo.getMateria().getIdMateria()));
        if (!habilitado) {
            throw new ReglaNegocioException("El docente " + docente.getUsername()
                    + " no está habilitado para dictar " + grupo.getMateria().getNombre());
        }

        Integer idPeriodo = grupo.getPeriodo().getIdPeriodo();
        for (FranjaHoraria franja : franjas) {
            docente.getRestriccionesHorarias().stream()
                    .filter(restriccion -> restriccion.getFranja().seCruzaCon(franja))
                    .findFirst()
                    .ifPresent(restriccion -> {
                        throw new ReglaNegocioException("La franja " + franja + " choca con una restricción horaria del docente ("
                                + restriccion.getFranja() + ")");
                    });

            detalleHorarioRepository.buscarCrucesDeDocente(docente.getIdUsuario(), idPeriodo, franja.getDiaSemana(),
                            franja.getHoraInicio(), franja.getHoraFin())
                    .stream()
                    .filter(sesion -> !sesion.getGrupo().getIdGrupo().equals(grupo.getIdGrupo()))
                    .findFirst()
                    .ifPresent(sesion -> {
                        throw new ReglaNegocioException("El docente ya dicta " + describir(sesion.getGrupo())
                                + " el " + sesion.getFranja());
                    });
        }

        if (docente.getTipoVinculacion() == null) {
            throw new ReglaNegocioException("El docente " + docente.getUsername() + " no tiene tipo de vinculación asignado");
        }
        double horasDisponibles = docenteService.calcularHorasDisponibles(docente.getIdUsuario(), idPeriodo);
        if (horasNuevas > horasDisponibles) {
            throw new ReglaNegocioException("El docente solo tiene " + horasDisponibles
                    + " horas disponibles y se requieren " + horasNuevas);
        }
    }

    /**
     * Notifica a los estudiantes en curso el cierre de un grupo e indica los grupos alternativos con cupo.
     *
     * @param grupo el grupo cerrado
     */
    private void notificarCierre(Grupo grupo) {
        List<InscripcionMateria> afectados = inscripcionMateriaRepository.findByGrupo_IdGrupoAndEstado(
                grupo.getIdGrupo(), EstadoMateria.CURSANDO);
        if (afectados.isEmpty()) {
            return;
        }
        String alternativas = listarOferta(grupo.getPeriodo().getIdPeriodo(), grupo.getMateria().getIdMateria()).stream()
                .filter(otro -> !otro.getIdGrupo().equals(grupo.getIdGrupo()))
                .filter(otro -> calcularCuposDisponibles(otro.getIdGrupo()) > 0)
                .map(otro -> "- Grupo " + otro.getCodigo() + ": " + franjasDe(otro).stream()
                        .map(FranjaHoraria::toString)
                        .collect(Collectors.joining(", ")))
                .collect(Collectors.joining("\n"));
        String mensaje = "El grupo " + grupo.getCodigo() + " de " + grupo.getMateria().getNombre() + " fue cerrado.\n"
                + (alternativas.isEmpty()
                ? "Por ahora no hay grupos alternativos con cupo; la coordinación te contactará."
                : "Grupos alternativos con cupo:\n" + alternativas);
        for (InscripcionMateria inscripcion : afectados) {
            notificacionService.notificar(inscripcion.getEstudiante(), "Grupo cerrado: " + grupo.getMateria().getNombre(), mensaje);
        }
    }

    /**
     * Busca una sesión dentro de un grupo.
     *
     * @param grupo el grupo
     * @param idSesion el identificador de la sesión del grupo
     * @return la sesión encontrada
     * @throws RecursoNoEncontradoException si el grupo no tiene esa sesión
     */
    private DetalleHorario buscarSesion(Grupo grupo, Integer idSesion) {
        return grupo.getSesiones().stream()
                .filter(s -> s.getIdDetalleHorario().equals(idSesion))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("El grupo no tiene una sesión con id " + idSesion));
    }

    /**
     * Obtiene las franjas de un grupo.
     *
     * @param grupo el grupo
     * @return las franjas de sus sesiones
     */
    private List<FranjaHoraria> franjasDe(Grupo grupo) {
        return grupo.getSesiones().stream().map(DetalleHorario::getFranja).toList();
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

    /**
     * Genera el texto del aula para la bitácora.
     *
     * @param aula el aula, o {@code null}
     * @return el texto con el código del aula, o vacío si no hay aula
     */
    private String aulaTexto(Aula aula) {
        return aula == null ? "" : " en " + aula.getCodigo();
    }
}
