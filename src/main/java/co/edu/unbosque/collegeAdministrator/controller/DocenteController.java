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
 * Controlador REST de los docentes.
 */
@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

    /** El servicio de gestión de docentes. */
    private final DocenteServiceAPI docenteService;
    /** El servicio de tipos de vinculación docente. */
    private final TipoVinculacionServiceAPI tipoVinculacionService;
    /** El servicio de gestión de grupos y sesiones. */
    private final GrupoServiceAPI grupoService;
    /** El componente que da acceso al usuario autenticado. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea una instancia de {@code DocenteController} con las dependencias requeridas.
     *
     * @param docenteService el servicio de gestión de docentes
     * @param tipoVinculacionService el servicio de tipos de vinculación docente
     * @param grupoService el servicio de gestión de grupos y sesiones
     * @param usuarioActual el componente que da acceso al usuario autenticado
     */
    public DocenteController(DocenteServiceAPI docenteService,
                             TipoVinculacionServiceAPI tipoVinculacionService,
                             GrupoServiceAPI grupoService,
                             UsuarioActual usuarioActual) {
        this.docenteService = docenteService;
        this.tipoVinculacionService = tipoVinculacionService;
        this.grupoService = grupoService;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Crea un docente y le asigna su vinculación si se indica.
     *
     * <p>Endpoint: {@code POST /api/docentes}. Acceso: administrador.
     *
     * @param request los datos validados del docente
     * @return la respuesta 201 con el docente creado
     */
    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<DocenteResponse> crear(@Valid @RequestBody DocenteRequest request) {
        Docente docente = UsuarioMapper.nuevoDocente(request);
        if (request.idTipoVinculacion() != null) {
            docente.setTipoVinculacion(tipoVinculacionService.buscarPorId(request.idTipoVinculacion()));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.aResponse(docenteService.guardar(docente)));
    }

    /**
     * Actualiza el perfil de un docente.
     *
     * <p>Endpoint: {@code PUT /api/docentes/{id}}. Acceso: administrador.
     *
     * @param id el identificador del docente
     * @param request los datos validados del perfil
     * @return los datos actualizados
     */
    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public DocenteResponse actualizarPerfil(@PathVariable Integer id, @Valid @RequestBody DocentePerfilRequest request) {
        Docente docente = docenteService.buscarPorId(id);
        docente.setCorreo(request.correo());
        docente.setEscalafon(request.escalafon());
        docente.setEspecialidad(request.especialidad());
        return UsuarioMapper.aResponse(docenteService.guardar(docente));
    }

    /**
     * Asigna un tipo de vinculación a un docente.
     *
     * <p>Endpoint: {@code PUT /api/docentes/{id}/vinculacion/{idTipoVinculacion}}. Acceso: administrador.
     *
     * @param id el identificador del docente
     * @param idTipoVinculacion el identificador del tipo de vinculación
     * @return los datos actualizados
     */
    @PutMapping("/{id}/vinculacion/{idTipoVinculacion}")
    @PreAuthorize(Permisos.ADMIN)
    public DocenteResponse asignarVinculacion(@PathVariable Integer id, @PathVariable Integer idTipoVinculacion) {
        return UsuarioMapper.aResponse(docenteService.asignarTipoVinculacion(id, idTipoVinculacion));
    }

    /**
     * Habilita a un docente para una materia.
     *
     * <p>Endpoint: {@code POST /api/docentes/{id}/materias-habilitadas/{idMateria}}. Acceso: administrador.
     *
     * @param id el identificador del docente
     * @param idMateria el identificador de la materia
     * @return los datos actualizados
     */
    @PostMapping("/{id}/materias-habilitadas/{idMateria}")
    @PreAuthorize(Permisos.ADMIN)
    public DocenteResponse habilitarMateria(@PathVariable Integer id, @PathVariable Integer idMateria) {
        return UsuarioMapper.aResponse(docenteService.habilitarMateria(id, idMateria));
    }

    /**
     * Retira la habilitación de un docente para una materia.
     *
     * <p>Endpoint: {@code DELETE /api/docentes/{id}/materias-habilitadas/{idMateria}}. Acceso: administrador.
     *
     * @param id el identificador del docente
     * @param idMateria el identificador de la materia
     * @return los datos actualizados
     */
    @DeleteMapping("/{id}/materias-habilitadas/{idMateria}")
    @PreAuthorize(Permisos.ADMIN)
    public DocenteResponse deshabilitarMateria(@PathVariable Integer id, @PathVariable Integer idMateria) {
        return UsuarioMapper.aResponse(docenteService.deshabilitarMateria(id, idMateria));
    }

    // HU-07: el docente registra sus propias franjas bloqueadas.
    /**
     * Registra una franja bloqueada del docente (HU-07); solo el propio docente o un administrador.
     *
     * <p>Endpoint: {@code POST /api/docentes/{id}/restricciones}. Acceso: docente o administrador.
     *
     * @param id el identificador del docente
     * @param request la franja bloqueada
     * @return la respuesta 201 con el docente actualizado
     */
    @PostMapping("/{id}/restricciones")
    @PreAuthorize(Permisos.DOCENTE_O_ADMIN)
    public ResponseEntity<DocenteResponse> agregarRestriccion(@PathVariable Integer id, @Valid @RequestBody FranjaRequest request) {
        usuarioActual.verificarPropietarioOAdmin(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioMapper.aResponse(docenteService.agregarRestriccion(id, request.aFranja())));
    }

    /**
     * Quita una franja bloqueada; solo el propio docente o un administrador.
     *
     * <p>Endpoint: {@code DELETE /api/docentes/{id}/restricciones/{idRestriccion}}. Acceso: docente o
     * administrador.
     *
     * @param id el identificador del docente
     * @param idRestriccion el identificador de la restricción horaria
     * @return los datos actualizados
     */
    @DeleteMapping("/{id}/restricciones/{idRestriccion}")
    @PreAuthorize(Permisos.DOCENTE_O_ADMIN)
    public DocenteResponse quitarRestriccion(@PathVariable Integer id, @PathVariable Integer idRestriccion) {
        usuarioActual.verificarPropietarioOAdmin(id);
        return UsuarioMapper.aResponse(docenteService.quitarRestriccion(id, idRestriccion));
    }

    /**
     * Lista los docentes.
     *
     * <p>Endpoint: {@code GET /api/docentes}. Acceso: director de programa, decano o administrador.
     *
     * @return todos los docentes
     */
    @GetMapping
    @PreAuthorize(Permisos.DIRECTIVOS)
    public List<DocenteResponse> listar() {
        return docenteService.listar().stream().map(UsuarioMapper::aResponse).toList();
    }

    /**
     * Consulta un docente, si el solicitante tiene acceso.
     *
     * <p>Endpoint: {@code GET /api/docentes/{id}}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del docente
     * @return los datos del docente
     */
    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public DocenteResponse buscar(@PathVariable Integer id) {
        verificarAcceso(id);
        return UsuarioMapper.aResponse(docenteService.buscarPorId(id));
    }

    // HU-08: carga asignada frente a la disponibilidad máxima del docente en el periodo.
    /**
     * Consulta la carga asignada frente a la disponibilidad máxima del docente (HU-08).
     *
     * <p>Endpoint: {@code GET /api/docentes/{id}/carga}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del docente
     * @param idPeriodo el identificador del periodo académico
     * @return la carga horaria en el periodo
     */
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
    /**
     * Consulta la asignación del docente, en borrador o publicada (HU-10, HU-55).
     *
     * <p>Endpoint: {@code GET /api/docentes/{id}/horario}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del docente
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos del docente en el periodo
     */
    @GetMapping("/{id}/horario")
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<GrupoResponse> horario(@PathVariable Integer id, @RequestParam Integer idPeriodo) {
        verificarAcceso(id);
        return grupoService.listarPorDocente(id, idPeriodo).stream()
                .map(grupo -> HorarioMapper.aResponse(grupo, grupoService.calcularCuposDisponibles(grupo.getIdGrupo())))
                .toList();
    }

    /**
     * Permite el acceso al propio docente, a administradores, directores y decanos.
     *
     * @param idDocente el identificador del docente consultado
     */
    private void verificarAcceso(Integer idDocente) {
        usuarioActual.verificarPropietarioOAlgunRol(idDocente, Rol.ADMINISTRADOR, Rol.DIRECTOR_PROGRAMA, Rol.DECANO);
    }
}
