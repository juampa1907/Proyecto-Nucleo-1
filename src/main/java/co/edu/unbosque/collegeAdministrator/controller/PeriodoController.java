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


@RestController
@RequestMapping("/api/periodos")
public class PeriodoController {

    private final PeriodoAcademicoServiceAPI periodoAcademicoService;
    private final GeneradorHorarioServiceAPI generadorHorarioService;
    private final VersionHorarioServiceAPI versionHorarioService;
    private final GrupoServiceAPI grupoService;
    private final ObservacionAsignacionServiceAPI observacionAsignacionService;
    private final PresupuestoVinculacionServiceAPI presupuestoVinculacionService;

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

    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<PeriodoResponse> listar() {
        return periodoAcademicoService.listar().stream().map(AcademicoMapper::aResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public PeriodoResponse buscar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(periodoAcademicoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<PeriodoResponse> crear(@Valid @RequestBody PeriodoRequest request) {
        PeriodoAcademico periodo = periodoAcademicoService.guardar(AcademicoMapper.nuevoPeriodo(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AcademicoMapper.aResponse(periodo));
    }

    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public PeriodoResponse actualizar(@PathVariable Integer id, @Valid @RequestBody PeriodoRequest request) {
        PeriodoAcademico periodo = periodoAcademicoService.buscarPorId(id);
        AcademicoMapper.aplicar(request, periodo);
        return AcademicoMapper.aResponse(periodoAcademicoService.guardar(periodo));
    }

    @PostMapping("/{id}/cerrar")
    @PreAuthorize(Permisos.ADMIN)
    public PeriodoResponse cerrar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(periodoAcademicoService.cerrar(id));
    }

    @PostMapping("/{id}/publicar")
    @PreAuthorize(Permisos.DIRECCION)
    public PeriodoResponse publicarHorario(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(periodoAcademicoService.publicarHorario(id));
    }

    @PostMapping("/{id}/generar-propuesta")
    @PreAuthorize(Permisos.DIRECCION)
    public ResultadoGeneracion generarPropuesta(@PathVariable Integer id) {
        return generadorHorarioService.generarPropuesta(id);
    }

    @GetMapping("/{id}/versiones")
    @PreAuthorize(Permisos.DIRECCION)
    public List<VersionHorarioResponse> versiones(@PathVariable Integer id) {
        return versionHorarioService.listarVersiones(id).stream().map(HorarioMapper::aResponse).toList();
    }


    @PostMapping("/versiones/{idVersion}/restaurar")
    @PreAuthorize(Permisos.DIRECCION)
    public VersionHorarioResponse restaurarVersion(@PathVariable Integer idVersion) {
        return HorarioMapper.aResponse(versionHorarioService.restaurar(idVersion));
    }

    @GetMapping("/{id}/grupos-en-riesgo")
    @PreAuthorize(Permisos.DIRECCION)
    public List<GrupoResponse> gruposEnRiesgo(@PathVariable Integer id) {
        return aResponses(grupoService.listarGruposEnRiesgo(id));
    }

    @PostMapping("/{id}/cerrar-grupos-bajo-minimo")
    @PreAuthorize(Permisos.DIRECCION)
    public List<GrupoResponse> cerrarGruposBajoMinimo(@PathVariable Integer id) {
        return aResponses(grupoService.cerrarGruposBajoMinimo(id));
    }

    @GetMapping("/{id}/observaciones-pendientes")
    @PreAuthorize(Permisos.DIRECCION)
    public List<ObservacionResponse> observacionesPendientes(@PathVariable Integer id) {
        return observacionAsignacionService.listarPendientes(id).stream().map(HorarioMapper::aResponse).toList();
    }

    @GetMapping("/{id}/presupuestos")
    @PreAuthorize(Permisos.DIRECTIVOS)
    public List<PresupuestoResponse> presupuestos(@PathVariable Integer id) {
        return presupuestoVinculacionService.listarPorPeriodo(id).stream().map(AdministracionMapper::aResponse).toList();
    }

    @PutMapping("/{id}/presupuestos")
    @PreAuthorize(Permisos.PRESUPUESTO)
    public PresupuestoResponse registrarPresupuesto(@PathVariable Integer id, @Valid @RequestBody PresupuestoRequest request) {
        return AdministracionMapper.aResponse(presupuestoVinculacionService.registrar(id, request.idTipoVinculacion(),
                request.horasPresupuestadas()));
    }

    private List<GrupoResponse> aResponses(List<Grupo> grupos) {
        return grupos.stream()
                .map(grupo -> HorarioMapper.aResponse(grupo, grupoService.calcularCuposDisponibles(grupo.getIdGrupo())))
                .toList();
    }
}
