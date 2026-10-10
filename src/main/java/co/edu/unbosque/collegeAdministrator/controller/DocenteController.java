package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.HorarioMapper;
import co.edu.unbosque.collegeAdministrator.dto.mapper.UsuarioMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.DocentePerfilRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.DocenteRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.FranjaRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.CargaHorariaResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.DocenteResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.GrupoResponse;
import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import co.edu.unbosque.collegeAdministrator.security.UsuarioActual;
import co.edu.unbosque.collegeAdministrator.service.api.DocenteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.GrupoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.TipoVinculacionServiceAPI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HU-05 a HU-10 y HU-55. El perfil lo administra el ADMINISTRADOR; las restricciones horarias las registra
 * el propio docente (HU-07); las consultas las ve el docente, el director, el decano o el administrador.
 */
@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

    private final DocenteServiceAPI docenteService;
    private final TipoVinculacionServiceAPI tipoVinculacionService;
    private final GrupoServiceAPI grupoService;
    private final UsuarioActual usuarioActual;

    public DocenteController(DocenteServiceAPI docenteService,
                             TipoVinculacionServiceAPI tipoVinculacionService,
                             GrupoServiceAPI grupoService,
                             UsuarioActual usuarioActual) {
        this.docenteService = docenteService;
        this.tipoVinculacionService = tipoVinculacionService;
        this.grupoService = grupoService;
        this.usuarioActual = usuarioActual;
    }

    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<DocenteResponse> crear(@Valid @RequestBody DocenteRequest request) {
        Docente docente = UsuarioMapper.nuevoDocente(request);
        if (request.idTipoVinculacion() != null) {
            docente.setTipoVinculacion(tipoVinculacionService.buscarPorId(request.idTipoVinculacion()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.aResponse(docenteService.guardar(docente)));
    }

    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public DocenteResponse actualizarPerfil(@PathVariable Integer id, @Valid @RequestBody DocentePerfilRequest request) {
        Docente docente = docenteService.buscarPorId(id);
        docente.setCorreo(request.correo());
        docente.setEscalafon(request.escalafon());
        docente.setEspecialidad(request.especialidad());
        return UsuarioMapper.aResponse(docenteService.guardar(docente));
    }

    @PutMapping("/{id}/vinculacion/{idTipoVinculacion}")
    @PreAuthorize(Permisos.ADMIN)
    public DocenteResponse asignarVinculacion(@PathVariable Integer id, @PathVariable Integer idTipoVinculacion) {
        return UsuarioMapper.aResponse(docenteService.asignarTipoVinculacion(id, idTipoVinculacion));
    }

    @PostMapping("/{id}/materias-habilitadas/{idMateria}")
    @PreAuthorize(Permisos.ADMIN)
    public DocenteResponse habilitarMateria(@PathVariable Integer id, @PathVariable Integer idMateria) {
        return UsuarioMapper.aResponse(docenteService.habilitarMateria(id, idMateria));
    }

    @DeleteMapping("/{id}/materias-habilitadas/{idMateria}")
    @PreAuthorize(Permisos.ADMIN)
    public DocenteResponse deshabilitarMateria(@PathVariable Integer id, @PathVariable Integer idMateria) {
        return UsuarioMapper.aResponse(docenteService.deshabilitarMateria(id, idMateria));
    }

    // HU-07: el docente registra sus propias franjas bloqueadas.
    @PostMapping("/{id}/restricciones")
    @PreAuthorize(Permisos.DOCENTE_O_ADMIN)
    public ResponseEntity<DocenteResponse> agregarRestriccion(@PathVariable Integer id, @Valid @RequestBody FranjaRequest request) {
        usuarioActual.verificarPropietarioOAdmin(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioMapper.aResponse(docenteService.agregarRestriccion(id, request.aFranja())));
    }

    @DeleteMapping("/{id}/restricciones/{idRestriccion}")
    @PreAuthorize(Permisos.DOCENTE_O_ADMIN)
    public DocenteResponse quitarRestriccion(@PathVariable Integer id, @PathVariable Integer idRestriccion) {
        usuarioActual.verificarPropietarioOAdmin(id);
        return UsuarioMapper.aResponse(docenteService.quitarRestriccion(id, idRestriccion));
    }

    @GetMapping
    @PreAuthorize(Permisos.DIRECTIVOS)
    public List<DocenteResponse> listar() {
        return docenteService.listar().stream().map(UsuarioMapper::aResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public DocenteResponse buscar(@PathVariable Integer id) {
        verificarAcceso(id);
        return UsuarioMapper.aResponse(docenteService.buscarPorId(id));
    }

    // HU-08: carga asignada frente a la disponibilidad máxima del docente en el periodo.
    @GetMapping("/{id}/carga")
    @PreAuthorize(Permisos.AUTENTICADO)
    public CargaHorariaResponse carga(@PathVariable Integer id, @RequestParam Integer idPeriodo) {
        verificarAcceso(id);
        Docente docente = docenteService.buscarPorId(id);
        double asignadas = docenteService.calcularHorasAsignadas(id, idPeriodo);
        if (docente.getTipoVinculacion() == null) {
            return new CargaHorariaResponse(id, idPeriodo, null, asignadas, null);
        }
        return new CargaHorariaResponse(id, idPeriodo, docente.getTipoVinculacion().getHorasMaximasSemanales(),
                asignadas, docenteService.calcularHorasDisponibles(id, idPeriodo));
    }

    // HU-10 / HU-55: el docente ve su asignación (en borrador o publicada; el estado está en el periodo).
    @GetMapping("/{id}/horario")
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<GrupoResponse> horario(@PathVariable Integer id, @RequestParam Integer idPeriodo) {
        verificarAcceso(id);
        return grupoService.listarPorDocente(id, idPeriodo).stream()
                .map(grupo -> HorarioMapper.aResponse(grupo, grupoService.calcularCuposDisponibles(grupo.getIdGrupo())))
                .toList();
    }

    private void verificarAcceso(Integer idDocente) {
        usuarioActual.verificarPropietarioOAlgunRol(idDocente, Rol.ADMINISTRADOR, Rol.DIRECTOR_PROGRAMA, Rol.DECANO);
    }
}
