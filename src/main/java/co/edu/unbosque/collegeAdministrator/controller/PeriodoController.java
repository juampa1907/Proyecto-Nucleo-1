package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.ResultadoGeneracion;
import co.edu.unbosque.collegeAdministrator.dto.mapper.AcademicoMapper;
import co.edu.unbosque.collegeAdministrator.dto.mapper.AdministracionMapper;
import co.edu.unbosque.collegeAdministrator.dto.mapper.HorarioMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.PeriodoRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.PresupuestoRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.GrupoResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.ObservacionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.PeriodoResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.PresupuestoResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.VersionHorarioResponse;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.service.api.GeneradorHorarioServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.GrupoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ObservacionAsignacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PresupuestoVinculacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.VersionHorarioServiceAPI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controlador REST de los periodos académicos, su horario y sus presupuestos.
 */
@RestController
@RequestMapping("/api/periodos")
public class PeriodoController {

    /** El servicio de gestión de periodos académicos. */
    private final PeriodoAcademicoServiceAPI periodoAcademicoService;
    /** El servicio que genera la propuesta automática de horario. */
    private final GeneradorHorarioServiceAPI generadorHorarioService;
    /** El servicio de versiones del horario. */
    private final VersionHorarioServiceAPI versionHorarioService;
    /** El servicio de gestión de grupos y sesiones. */
    private final GrupoServiceAPI grupoService;
    /** El servicio de observaciones de asignación. */
    private final ObservacionAsignacionServiceAPI observacionAsignacionService;
    /** El servicio de presupuestos por vinculación. */
    private final PresupuestoVinculacionServiceAPI presupuestoVinculacionService;

    /**
     * Crea una instancia de {@code PeriodoController} con las dependencias requeridas.
     *
     * @param periodoAcademicoService el servicio de gestión de periodos académicos
     * @param generadorHorarioService el servicio que genera la propuesta automática de horario
     * @param versionHorarioService el servicio de versiones del horario
     * @param grupoService el servicio de gestión de grupos y sesiones
     * @param observacionAsignacionService el servicio de observaciones de asignación
     * @param presupuestoVinculacionService el servicio de presupuestos por vinculación
     */
    public PeriodoController(PeriodoAcademicoServiceAPI periodoAcademicoService,
                             GeneradorHorarioServiceAPI generadorHorarioService,
                             VersionHorarioServiceAPI versionHorarioService,
                             GrupoServiceAPI grupoService,
                             ObservacionAsignacionServiceAPI observacionAsignacionService,
                             PresupuestoVinculacionServiceAPI presupuestoVinculacionService) {
        this.periodoAcademicoService = periodoAcademicoService;
        this.generadorHorarioService = generadorHorarioService;
        this.versionHorarioService = versionHorarioService;
        this.grupoService = grupoService;
        this.observacionAsignacionService = observacionAsignacionService;
        this.presupuestoVinculacionService = presupuestoVinculacionService;
    }

    /**
     * Lista los periodos.
     *
     * <p>Endpoint: {@code GET /api/periodos}. Acceso: cualquier usuario autenticado.
     *
     * @return todos los periodos
     */
    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<PeriodoResponse> listar() {
        return periodoAcademicoService.listar().stream().map(AcademicoMapper::aResponse).toList();
    }

    /**
     * Consulta un periodo.
     *
     * <p>Endpoint: {@code GET /api/periodos/{id}}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del periodo
     * @return los datos del periodo
     */
    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public PeriodoResponse buscar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(periodoAcademicoService.buscarPorId(id));
    }

    /**
     * Crea un periodo.
     *
     * <p>Endpoint: {@code POST /api/periodos}. Acceso: administrador.
     *
     * @param request los datos validados del periodo
     * @return la respuesta 201 con el periodo creado
     */
    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<PeriodoResponse> crear(@Valid @RequestBody PeriodoRequest request) {
        PeriodoAcademico periodo = periodoAcademicoService.guardar(AcademicoMapper.nuevoPeriodo(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AcademicoMapper.aResponse(periodo));
    }

    /**
     * Actualiza un periodo.
     *
     * <p>Endpoint: {@code PUT /api/periodos/{id}}. Acceso: administrador.
     *
     * @param id el identificador del periodo
     * @param request los datos validados del periodo
     * @return los datos actualizados
     */
    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public PeriodoResponse actualizar(@PathVariable Integer id, @Valid @RequestBody PeriodoRequest request) {
        PeriodoAcademico periodo = periodoAcademicoService.buscarPorId(id);
        AcademicoMapper.aplicar(request, periodo);
        return AcademicoMapper.aResponse(periodoAcademicoService.guardar(periodo));
    }

    /**
     * Cierra un periodo.
     *
     * <p>Endpoint: {@code POST /api/periodos/{id}/cerrar}. Acceso: administrador.
     *
     * @param id el identificador del periodo
     * @return los datos actualizados
     */
    @PostMapping("/{id}/cerrar")
    @PreAuthorize(Permisos.ADMIN)
    public PeriodoResponse cerrar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(periodoAcademicoService.cerrar(id));
    }

    /**
     * Publica el horario del periodo.
     *
     * <p>Endpoint: {@code POST /api/periodos/{id}/publicar}. Acceso: director de programa o administrador.
     *
     * @param id el identificador del periodo
     * @return los datos actualizados
     */
    @PostMapping("/{id}/publicar")
    @PreAuthorize(Permisos.DIRECCION)
    public PeriodoResponse publicarHorario(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(periodoAcademicoService.publicarHorario(id));
    }

    /**
     * Genera la propuesta automática de horario.
     *
     * <p>Endpoint: {@code POST /api/periodos/{id}/generar-propuesta}. Acceso: director de programa o
     * administrador.
     *
     * @param id el identificador del periodo
     * @return los grupos programados y los no programados
     */
    @PostMapping("/{id}/generar-propuesta")
    @PreAuthorize(Permisos.DIRECCION)
    public ResultadoGeneracion generarPropuesta(@PathVariable Integer id) {
        return generadorHorarioService.generarPropuesta(id);
    }

    /**
     * Lista las versiones del horario.
     *
     * <p>Endpoint: {@code GET /api/periodos/{id}/versiones}. Acceso: director de programa o administrador.
     *
     * @param id el identificador del periodo
     * @return las versiones del periodo
     */
    @GetMapping("/{id}/versiones")
    @PreAuthorize(Permisos.DIRECCION)
    public List<VersionHorarioResponse> versiones(@PathVariable Integer id) {
        return versionHorarioService.listarVersiones(id).stream().map(HorarioMapper::aResponse).toList();
    }


    /**
     * Restaura una versión del horario.
     *
     * <p>Endpoint: {@code POST /api/periodos/versiones/{idVersion}/restaurar}. Acceso: director de programa o
     * administrador.
     *
     * @param idVersion el identificador de la versión del horario
     * @return la copia de respaldo del estado anterior
     */
    @PostMapping("/versiones/{idVersion}/restaurar")
    @PreAuthorize(Permisos.DIRECCION)
    public VersionHorarioResponse restaurarVersion(@PathVariable Integer idVersion) {
        return HorarioMapper.aResponse(versionHorarioService.restaurar(idVersion));
    }

    /**
     * Lista los grupos en riesgo.
     *
     * <p>Endpoint: {@code GET /api/periodos/{id}/grupos-en-riesgo}. Acceso: director de programa o
     * administrador.
     *
     * @param id el identificador del periodo
     * @return los grupos bajo el mínimo de estudiantes
     */
    @GetMapping("/{id}/grupos-en-riesgo")
    @PreAuthorize(Permisos.DIRECCION)
    public List<GrupoResponse> gruposEnRiesgo(@PathVariable Integer id) {
        return aResponses(grupoService.listarGruposEnRiesgo(id));
    }

    /**
     * Cierra los grupos bajo el mínimo de estudiantes.
     *
     * <p>Endpoint: {@code POST /api/periodos/{id}/cerrar-grupos-bajo-minimo}. Acceso: director de programa o
     * administrador.
     *
     * @param id el identificador del periodo
     * @return los grupos cerrados
     */
    @PostMapping("/{id}/cerrar-grupos-bajo-minimo")
    @PreAuthorize(Permisos.DIRECCION)
    public List<GrupoResponse> cerrarGruposBajoMinimo(@PathVariable Integer id) {
        return aResponses(grupoService.cerrarGruposBajoMinimo(id));
    }

    /**
     * Lista las observaciones pendientes.
     *
     * <p>Endpoint: {@code GET /api/periodos/{id}/observaciones-pendientes}. Acceso: director de programa o
     * administrador.
     *
     * @param id el identificador del periodo
     * @return las observaciones por resolver
     */
    @GetMapping("/{id}/observaciones-pendientes")
    @PreAuthorize(Permisos.DIRECCION)
    public List<ObservacionResponse> observacionesPendientes(@PathVariable Integer id) {
        return observacionAsignacionService.listarPendientes(id).stream().map(HorarioMapper::aResponse).toList();
    }

    /**
     * Lista los presupuestos del periodo.
     *
     * <p>Endpoint: {@code GET /api/periodos/{id}/presupuestos}. Acceso: director de programa, decano o
     * administrador.
     *
     * @param id el identificador del periodo
     * @return los presupuestos por vinculación
     */
    @GetMapping("/{id}/presupuestos")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public List<PresupuestoResponse> presupuestos(@PathVariable Integer id) {
        return presupuestoVinculacionService.listarPorPeriodo(id).stream().map(AdministracionMapper::aResponse).toList();
    }

    /**
     * Crea o actualiza un presupuesto.
     *
     * <p>Endpoint: {@code PUT /api/periodos/{id}/presupuestos}. Acceso: decano o administrador.
     *
     * @param id el identificador del periodo
     * @param request los datos validados del presupuesto
     * @return el presupuesto guardado
     */
    @PutMapping("/{id}/presupuestos")
    @PreAuthorize(Permisos.PRESUPUESTO)
    public PresupuestoResponse registrarPresupuesto(@PathVariable Integer id, @Valid @RequestBody PresupuestoRequest request) {
        return AdministracionMapper.aResponse(presupuestoVinculacionService.registrar(id, request.idTipoVinculacion(),
                request.horasPresupuestadas()));
    }

    /**
     * Convierte grupos en respuestas con sus cupos disponibles.
     *
     * @param grupos los grupos
     * @return las respuestas correspondientes
     */
    private List<GrupoResponse> aResponses(List<Grupo> grupos) {
        return grupos.stream()
                .map(grupo -> HorarioMapper.aResponse(grupo, grupoService.calcularCuposDisponibles(grupo.getIdGrupo())))
                .toList();
    }
}
