package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.dto.ResultadoReasignacion;
import co.edu.unbosque.collegeAdministrator.entity.DetalleHorario;
import co.edu.unbosque.collegeAdministrator.entity.Estudiante;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.entity.MatriculaPeriodo;
import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;
import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import co.edu.unbosque.collegeAdministrator.repository.InscripcionMateriaRepository;
import co.edu.unbosque.collegeAdministrator.repository.MatriculaPeriodoRepository;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.EstudianteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.GrupoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.InscripcionMateriaServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.NotificacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ParametrosAcademicosServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class InscripcionMateriaServiceImpl extends GenericServiceImpl<InscripcionMateria, Integer>
        implements InscripcionMateriaServiceAPI {

    static final BigDecimal NOTA_MINIMA = new BigDecimal("0.0");
    static final BigDecimal NOTA_MAXIMA = new BigDecimal("5.0");
    static final BigDecimal NOTA_APROBACION = new BigDecimal("3.0");

    private final InscripcionMateriaRepository inscripcionMateriaRepository;
    private final MatriculaPeriodoRepository matriculaPeriodoRepository;
    private final EstudianteServiceAPI estudianteService;
    private final GrupoServiceAPI grupoService;
    private final PeriodoAcademicoServiceAPI periodoAcademicoService;
    private final ParametrosAcademicosServiceAPI parametrosAcademicosService;
    private final NotificacionServiceAPI notificacionService;
    private final BitacoraServiceAPI bitacoraService;

    public InscripcionMateriaServiceImpl(InscripcionMateriaRepository inscripcionMateriaRepository,
                                         MatriculaPeriodoRepository matriculaPeriodoRepository,
                                         EstudianteServiceAPI estudianteService,
                                         GrupoServiceAPI grupoService,
                                         PeriodoAcademicoServiceAPI periodoAcademicoService,
                                         ParametrosAcademicosServiceAPI parametrosAcademicosService,
                                         NotificacionServiceAPI notificacionService,
                                         BitacoraServiceAPI bitacoraService) {
        this.inscripcionMateriaRepository = inscripcionMateriaRepository;
        this.matriculaPeriodoRepository = matriculaPeriodoRepository;
        this.estudianteService = estudianteService;
        this.grupoService = grupoService;
        this.periodoAcademicoService = periodoAcademicoService;
        this.parametrosAcademicosService = parametrosAcademicosService;
        this.notificacionService = notificacionService;
        this.bitacoraService = bitacoraService;
    }

    @Override
    protected JpaRepository<InscripcionMateria, Integer> getRepository() {
        return inscripcionMateriaRepository;
    }

    @Override
    public InscripcionMateria inscribir(Integer idEstudiante, Integer idGrupo) {
        Estudiante estudiante = estudianteService.buscarPorId(idEstudiante);
        Grupo grupo = grupoService.buscarPorId(idGrupo);
        Materia materia = grupo.getMateria();

        PeriodoAcademico periodo = grupo.getPeriodo();
        validarPeriodoDeInscripcion(periodo);
        if (!periodo.horarioPublicado()) {
            throw new ReglaNegocioException("El horario del periodo " + periodo.getCodigo() + " aún no ha sido publicado");
        }
        if (grupo.getEstado() != EstadoGrupo.ABIERTO) {
            throw new ReglaNegocioException("El grupo " + grupo.getCodigo() + " de " + materia.getNombre() + " está cerrado");
        }
        if (inscripcionMateriaRepository.existsByEstudiante_IdUsuarioAndGrupo_IdGrupo(idEstudiante, idGrupo)) {
            throw new ReglaNegocioException("Ya estás inscrito en el grupo " + grupo.getCodigo() + " de " + materia.getNombre());
        }
        validarHistorial(estudiante, materia);
        validarPrerrequisitos(estudiante, materia);
        validarCorrequisitos(estudiante, materia);
        validarCreditosMaximos(estudiante, materia);
        if (grupoService.calcularCuposDisponibles(idGrupo) <= 0) {
            throw new ReglaNegocioException("El grupo " + grupo.getCodigo() + " de " + materia.getNombre()
                    + " no tiene cupos disponibles");
        }
        String cruce = buscarCruce(estudiante, grupo, null);
        if (cruce != null) {
            throw new ReglaNegocioException(cruce);
        }

        InscripcionMateria inscripcion = new InscripcionMateria(grupo, EstadoMateria.CURSANDO);
        estudiante.agregarInscripcion(inscripcion);
        return inscripcionMateriaRepository.save(inscripcion);
    }

    @Override
    public void cancelar(Integer idInscripcion) {
        InscripcionMateria inscripcion = buscarPorId(idInscripcion);
        if (inscripcion.getEstado() != EstadoMateria.CURSANDO) {
            throw new ReglaNegocioException("Solo se pueden cancelar materias en estado CURSANDO");
        }
        PeriodoAcademico periodo = inscripcion.getGrupo().getPeriodo();
        validarPeriodoDeInscripcion(periodo);
        validarMinimoTrasCancelar(inscripcion, periodo);
        inscripcion.getEstudiante().quitarInscripcion(inscripcion);
    }

    @Override
    public InscripcionMateria registrarNota(Integer idInscripcion, BigDecimal nota) {
        if (nota == null || nota.compareTo(NOTA_MINIMA) < 0 || nota.compareTo(NOTA_MAXIMA) > 0) {
            throw new ReglaNegocioException("La nota debe estar entre " + NOTA_MINIMA + " y " + NOTA_MAXIMA);
        }
        InscripcionMateria inscripcion = buscarPorId(idInscripcion);
        if (inscripcion.getGrupo().getPeriodo().estaCerrado()) {
            throw new ReglaNegocioException("El periodo está cerrado y es de solo lectura");
        }
        if (inscripcion.getEstado() == EstadoMateria.RETIRADA) {
            throw new ReglaNegocioException("No se puede registrar nota a una materia retirada");
        }
        inscripcion.setNotaFinal(nota);
        inscripcion.setEstado(nota.compareTo(NOTA_APROBACION) >= 0 ? EstadoMateria.APROBADA : EstadoMateria.REPROBADA);
        return inscripcion;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionMateria> listarPorEstudianteYEstado(Integer idEstudiante, EstadoMateria estado) {
        return inscripcionMateriaRepository.findByEstudiante_IdUsuarioAndEstado(idEstudiante, estado);
    }

    @Override
    public ResultadoReasignacion reasignarEstudiantes(Integer idGrupoOrigen, Integer idGrupoDestino) {
        Grupo origen = grupoService.buscarPorId(idGrupoOrigen);
        Grupo destino = grupoService.buscarPorId(idGrupoDestino);
        if (origen.getEstado() != EstadoGrupo.CERRADO) {
            throw new ReglaNegocioException("Solo se reasignan estudiantes de un grupo cerrado");
        }
        if (destino.getEstado() != EstadoGrupo.ABIERTO) {
            throw new ReglaNegocioException("El grupo destino " + destino.getCodigo() + " no está abierto");
        }
        if (!origen.getMateria().getIdMateria().equals(destino.getMateria().getIdMateria())
                || !origen.getPeriodo().getIdPeriodo().equals(destino.getPeriodo().getIdPeriodo())) {
            throw new ReglaNegocioException("El grupo destino debe ser de la misma materia y periodo");
        }
        if (origen.getPeriodo().estaCerrado()) {
            throw new ReglaNegocioException("El periodo " + origen.getPeriodo().getCodigo() + " está cerrado");
        }

        long cupos = grupoService.calcularCuposDisponibles(idGrupoDestino);
        List<String> reasignados = new ArrayList<>();
        Map<String, String> noReasignados = new LinkedHashMap<>();
        String materia = destino.getMateria().getNombre();
        for (InscripcionMateria inscripcion : inscripcionMateriaRepository.findByGrupo_IdGrupoAndEstado(
                idGrupoOrigen, EstadoMateria.CURSANDO)) {
            Estudiante estudiante = inscripcion.getEstudiante();
            String motivo = cupos <= 0 ? "El grupo " + destino.getCodigo() + " no tiene cupos disponibles"
                    : buscarCruce(estudiante, destino, inscripcion);
            if (motivo == null) {
                inscripcion.setGrupo(destino);
                cupos--;
                reasignados.add(estudiante.getUsername());
                notificacionService.notificar(estudiante, "Reasignación de grupo: " + materia,
                        "Fuiste reasignado del grupo " + origen.getCodigo() + " al grupo " + destino.getCodigo()
                                + " de " + materia + ".");
            } else {
                noReasignados.put(estudiante.getUsername(), motivo);
                notificacionService.notificar(estudiante, "No fue posible reasignarte: " + materia,
                        "No se pudo moverte al grupo " + destino.getCodigo() + " de " + materia + ": " + motivo
                                + ". Revisa la oferta de grupos o contacta a la coordinación.");
            }
        }
        bitacoraService.registrar("REASIGNAR_ESTUDIANTES", materia + ": grupo " + origen.getCodigo() + " → "
                + destino.getCodigo() + " (" + reasignados.size() + " reasignados, " + noReasignados.size() + " no)");
        return new ResultadoReasignacion(reasignados, noReasignados);
    }

    @Override
    public MatriculaPeriodo confirmarMatricula(Integer idEstudiante, Integer idPeriodo) {
        Estudiante estudiante = estudianteService.buscarPorId(idEstudiante);
        PeriodoAcademico periodo = periodoAcademicoService.buscarPorId(idPeriodo);
        validarPeriodoDeInscripcion(periodo);
        int creditos = creditosEnCurso(estudiante, idPeriodo, null);
        if (creditos == 0) {
            throw new ReglaNegocioException("No tienes materias inscritas en el periodo " + periodo.getCodigo());
        }
        ParametrosAcademicos parametros = parametrosAcademicosService.obtener();
        if (parametros.getCreditosMinimos() != null && creditos < parametros.getCreditosMinimos()) {
            throw new ReglaNegocioException("Tienes " + creditos + " créditos y el mínimo es " + parametros.getCreditosMinimos());
        }
        if (parametros.getCreditosMaximos() != null && creditos > parametros.getCreditosMaximos()) {
            throw new ReglaNegocioException("Tienes " + creditos + " créditos y el máximo es " + parametros.getCreditosMaximos());
        }
        MatriculaPeriodo matricula = matriculaPeriodoRepository
                .findByEstudiante_IdUsuarioAndPeriodo_IdPeriodo(idEstudiante, idPeriodo)
                .orElseGet(() -> new MatriculaPeriodo(estudiante, periodo));
        matricula.setFechaConfirmacion(LocalDateTime.now());
        return matriculaPeriodoRepository.save(matricula);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionMateria> listarCargaDelPeriodo(Integer idEstudiante, Integer idPeriodo) {
        return estudianteService.buscarPorId(idEstudiante).getHistorialInscripciones().stream()
                .filter(ins -> ins.getGrupo().getPeriodo().getIdPeriodo().equals(idPeriodo))
                .filter(ins -> ins.getEstado() != EstadoMateria.RETIRADA)
                .toList();
    }

    private void validarPeriodoDeInscripcion(PeriodoAcademico periodo) {
        if (periodo.estaCerrado()) {
            throw new ReglaNegocioException("El periodo " + periodo.getCodigo() + " está cerrado");
        }
        if (!periodo.inscripcionAbierta(LocalDate.now())) {
            throw new ReglaNegocioException("La inscripción del periodo " + periodo.getCodigo() + " está habilitada del "
                    + periodo.getFechaAperturaInscripcion() + " al " + periodo.getFechaCierreInscripcion());
        }
    }

    private void validarMinimoTrasCancelar(InscripcionMateria inscripcion, PeriodoAcademico periodo) {
        Integer minimo = parametrosAcademicosService.obtener().getCreditosMinimos();
        if (minimo == null) {
            return;
        }
        Estudiante estudiante = inscripcion.getEstudiante();
        boolean confirmada = matriculaPeriodoRepository
                .findByEstudiante_IdUsuarioAndPeriodo_IdPeriodo(estudiante.getIdUsuario(), periodo.getIdPeriodo())
                .map(MatriculaPeriodo::estaConfirmada)
                .orElse(false);
        int restantes = creditosEnCurso(estudiante, periodo.getIdPeriodo(), inscripcion);
        if (confirmada && restantes < minimo) {
            throw new ReglaNegocioException("Tu matrícula está confirmada: al cancelar quedarías con " + restantes
                    + " créditos y el mínimo es " + minimo);
        }
    }

    private int creditosEnCurso(Estudiante estudiante, Integer idPeriodo, InscripcionMateria excluida) {
        return estudiante.getHistorialInscripciones().stream()
                .filter(ins -> ins != excluida)
                .filter(ins -> ins.getEstado() == EstadoMateria.CURSANDO)
                .filter(ins -> ins.getGrupo().getPeriodo().getIdPeriodo().equals(idPeriodo))
                .mapToInt(ins -> ins.getGrupo().getMateria().getNumeroCreditos())
                .sum();
    }

    private void validarCorrequisitos(Estudiante estudiante, Materia materia) {
        Set<Integer> aprobadasOCursando = estudiante.getHistorialInscripciones().stream()
                .filter(ins -> ins.getEstado() == EstadoMateria.APROBADA || ins.getEstado() == EstadoMateria.CURSANDO)
                .map(ins -> ins.getGrupo().getMateria().getIdMateria())
                .collect(Collectors.toSet());
        for (Materia correquisito : materia.getCorrequisitos()) {
            if (!aprobadasOCursando.contains(correquisito.getIdMateria())) {
                throw new ReglaNegocioException("El correquisito " + correquisito.getNombre()
                        + " debe estar aprobado o inscrito antes de inscribir " + materia.getNombre());
            }
        }
    }

    private void validarCreditosMaximos(Estudiante estudiante, Materia materia) {
        Integer maximo = parametrosAcademicosService.obtener().getCreditosMaximos();
        if (maximo == null) {
            return;
        }
        int creditosEnCurso = estudiante.getMateriasActuales().stream()
                .mapToInt(Materia::getNumeroCreditos)
                .sum();
        if (creditosEnCurso + materia.getNumeroCreditos() > maximo) {
            throw new ReglaNegocioException("Con " + materia.getNombre() + " tendrías "
                    + (creditosEnCurso + materia.getNumeroCreditos()) + " créditos y el máximo es " + maximo);
        }
    }

    private void validarHistorial(Estudiante estudiante, Materia materia) {
        for (InscripcionMateria previa : estudiante.getHistorialInscripciones()) {
            if (!previa.getGrupo().getMateria().getIdMateria().equals(materia.getIdMateria())) {
                continue;
            }
            if (previa.getEstado() == EstadoMateria.CURSANDO) {
                throw new ReglaNegocioException("Ya estás cursando " + materia.getNombre());
            }
            if (previa.getEstado() == EstadoMateria.APROBADA) {
                throw new ReglaNegocioException("Ya aprobaste " + materia.getNombre());
            }
        }
    }

    private void validarPrerrequisitos(Estudiante estudiante, Materia materia) {
        Set<Integer> aprobadas = estudiante.getMateriasCursadas().stream()
                .map(Materia::getIdMateria)
                .collect(Collectors.toSet());
        for (Materia prerrequisito : materia.getPrerrequisitos()) {
            if (!aprobadas.contains(prerrequisito.getIdMateria())) {
                throw new ReglaNegocioException("Falta aprobar el prerrequisito " + prerrequisito.getNombre()
                        + " para inscribir " + materia.getNombre());
            }
        }
    }

    private String buscarCruce(Estudiante estudiante, Grupo grupoNuevo, InscripcionMateria excluida) {
        for (InscripcionMateria actual : estudiante.getHistorialInscripciones()) {
            if (actual == excluida || actual.getEstado() != EstadoMateria.CURSANDO) {
                continue;
            }
            for (DetalleHorario sesionActual : actual.getGrupo().getSesiones()) {
                for (DetalleHorario sesionNueva : grupoNuevo.getSesiones()) {
                    if (sesionActual.getFranja().seCruzaCon(sesionNueva.getFranja())) {
                        return "Cruce de horario con " + actual.getGrupo().getMateria().getNombre()
                                + " (grupo " + actual.getGrupo().getCodigo() + ") el " + sesionActual.getFranja();
                    }
                }
            }
        }
        return null;
    }
}
