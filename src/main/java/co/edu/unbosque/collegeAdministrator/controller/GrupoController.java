package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.ResultadoReasignacion;
import co.edu.unbosque.collegeAdministrator.dto.mapper.AcademicoMapper;
import co.edu.unbosque.collegeAdministrator.dto.mapper.HorarioMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.GrupoRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.ObservacionRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.SesionRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.AulaResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.CuposResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.GrupoResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.ObservacionResponse;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.enums.DiaSemana;
import co.edu.unbosque.collegeAdministrator.security.UsuarioActual;
import co.edu.unbosque.collegeAdministrator.service.api.GrupoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.InscripcionMateriaServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ObservacionAsignacionServiceAPI;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.List;

/**
 * Grupos y su programación (HU-14, HU-18, HU-19, HU-22 a HU-26, HU-30, HU-35, HU-36, HU-55).
 * La programación la hace el director de programa (o el administrador); la oferta la ve cualquier usuario.
 */
@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private final GrupoServiceAPI grupoService;
    private final InscripcionMateriaServiceAPI inscripcionMateriaService;
    private final ObservacionAsignacionServiceAPI observacionAsignacionService;
    private final UsuarioActual usuarioActual;

    public GrupoController(GrupoServiceAPI grupoService,
                           InscripcionMateriaServiceAPI inscripcionMateriaService,
                           ObservacionAsignacionServiceAPI observacionAsignacionService,
                           UsuarioActual usuarioActual) {
        this.grupoService = grupoService;
        this.inscripcionMateriaService = inscripcionMateriaService;
        this.observacionAsignacionService = observacionAsignacionService;
        this.usuarioActual = usuarioActual;
    }

    /**
     * HU-30: con idMateria devuelve la oferta (solo grupos abiertos) de esa materia; sin ella, todos los grupos del periodo.
     */
    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<GrupoResponse> listar(@RequestParam Integer idPeriodo, @RequestParam(required = false) Integer idMateria) {
        List<Grupo> grupos = idMateria == null
                ? grupoService.listarPorPeriodo(idPeriodo)
                : grupoService.listarOferta(idPeriodo, idMateria);
        return grupos.stream().map(this::aResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public GrupoResponse buscar(@PathVariable Integer id) {
        return aResponse(grupoService.buscarPorId(id));
    }

    @GetMapping("/{id}/cupos")
    @PreAuthorize(Permisos.AUTENTICADO)
    public CuposResponse cupos(@PathVariable Integer id) {
        Grupo grupo = grupoService.buscarPorId(id);
        return new CuposResponse(id, grupo.getCupoMaximo(), grupoService.calcularCuposDisponibles(id));
    }

    @PostMapping
    @PreAuthorize(Permisos.DIRECCION)
    public ResponseEntity<GrupoResponse> crear(@Valid @RequestBody GrupoRequest request) {
        Grupo grupo = grupoService.crearGrupo(request.idPeriodo(), request.idMateria(), request.codigo(), request.cupoMaximo());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(grupo));
    }

    @PutMapping("/{id}/docente/{idDocente}")
    @PreAuthorize(Permisos.DIRECCION)
    public GrupoResponse asignarDocente(@PathVariable Integer id, @PathVariable Integer idDocente) {
        return aResponse(grupoService.asignarDocente(id, idDocente));
    }

    @PostMapping("/{id}/sesiones")
    @PreAuthorize(Permisos.DIRECCION)
    public ResponseEntity<GrupoResponse> agregarSesion(@PathVariable Integer id, @Valid @RequestBody SesionRequest request) {
        Grupo grupo = grupoService.agregarSesion(id, request.franja().aFranja(), request.idAula(), request.usoExcepcional());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(grupo));
    }

    // HU-25: mover una sesión (arrastrar y soltar en el frontend).
    @PutMapping("/{id}/sesiones/{idSesion}")
    @PreAuthorize(Permisos.DIRECCION)
    public GrupoResponse moverSesion(@PathVariable Integer id, @PathVariable Integer idSesion,
                                     @Valid @RequestBody SesionRequest request) {
        return aResponse(grupoService.moverSesion(id, idSesion, request.franja().aFranja(), request.idAula(),
                request.usoExcepcional()));
    }

    @DeleteMapping("/{id}/sesiones/{idSesion}")
    @PreAuthorize(Permisos.DIRECCION)
    public GrupoResponse quitarSesion(@PathVariable Integer id, @PathVariable Integer idSesion) {
        return aResponse(grupoService.quitarSesion(id, idSesion));
    }

    /**
     * HU-23: advertencias de días consecutivos (no bloquean). La franja nueva es opcional; horas en formato HH:mm.
     */
    @GetMapping("/{id}/advertencias")
    @PreAuthorize(Permisos.DIRECCION)
    public List<String> advertencias(@PathVariable Integer id,
                                     @RequestParam(required = false) DiaSemana dia,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime horaInicio,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime horaFin) {
        FranjaHoraria franja = dia != null && horaInicio != null && horaFin != null
                ? new FranjaHoraria(dia, horaInicio, horaFin)
                : null;
        return grupoService.advertenciasDiasConsecutivos(id, franja);
    }

    @GetMapping("/{id}/aulas-sugeridas")
    @PreAuthorize(Permisos.DIRECCION)
    public List<AulaResponse> aulasSugeridas(@PathVariable Integer id) {
        return grupoService.sugerirAulas(id).stream().map(AcademicoMapper::aResponse).toList();
    }

    @PostMapping("/{id}/cerrar")
    @PreAuthorize(Permisos.DIRECCION)
    public GrupoResponse cerrar(@PathVariable Integer id) {
        return aResponse(grupoService.cerrarGrupo(id));
    }

    // HU-36: mueve a los estudiantes del grupo cerrado {id} al grupo {idDestino}.
    @PostMapping("/{id}/reasignar/{idDestino}")
    @PreAuthorize(Permisos.DIRECCION)
    public ResultadoReasignacion reasignar(@PathVariable Integer id, @PathVariable Integer idDestino) {
        return inscripcionMateriaService.reasignarEstudiantes(id, idDestino);
    }

    // HU-55: el docente asignado deja una observación sobre su asignación en borrador.
    @PostMapping("/{id}/observaciones")
    @PreAuthorize(Permisos.DOCENTE)
    public ResponseEntity<ObservacionResponse> registrarObservacion(@PathVariable Integer id,
                                                                    @Valid @RequestBody ObservacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(HorarioMapper.aResponse(
                observacionAsignacionService.registrar(id, usuarioActual.obtenerIdUsuario(), request.texto())));
    }

    @PatchMapping("/observaciones/{idObservacion}/resolver")
    @PreAuthorize(Permisos.DIRECCION)
    public ObservacionResponse resolverObservacion(@PathVariable Integer idObservacion) {
        return HorarioMapper.aResponse(observacionAsignacionService.resolver(idObservacion));
    }

    private GrupoResponse aResponse(Grupo grupo) {
        return HorarioMapper.aResponse(grupo, grupoService.calcularCuposDisponibles(grupo.getIdGrupo()));
    }
}
