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
 * Controlador REST de los grupos, su programación y sus observaciones.
 */
@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    /** El servicio de gestión de grupos y sesiones. */
    private final GrupoServiceAPI grupoService;
    /** El servicio de inscripción de materias. */
    private final InscripcionMateriaServiceAPI inscripcionMateriaService;
    /** El servicio de observaciones de asignación. */
    private final ObservacionAsignacionServiceAPI observacionAsignacionService;
    /** El componente que da acceso al usuario autenticado. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea una instancia de {@code GrupoController} con las dependencias requeridas.
     *
     * @param grupoService el servicio de gestión de grupos y sesiones
     * @param inscripcionMateriaService el servicio de inscripción de materias
     * @param observacionAsignacionService el servicio de observaciones de asignación
     * @param usuarioActual el componente que da acceso al usuario autenticado
     */
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
     * Lista los grupos de un periodo o la oferta abierta de una materia.
     *
     * <p>Endpoint: {@code GET /api/grupos}. Acceso: cualquier usuario autenticado.
     *
     * @param idPeriodo el identificador del periodo académico
     * @param idMateria la materia consultada, o {@code null} para todos los grupos
     * @return los grupos encontrados
     */
    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<GrupoResponse> listar(@RequestParam Integer idPeriodo, @RequestParam(required = false) Integer idMateria) {
        List<Grupo> grupos = idMateria == null
                ? grupoService.listarPorPeriodo(idPeriodo)
                : grupoService.listarOferta(idPeriodo, idMateria);
        return grupos.stream().map(this::aResponse).toList();
    }

    /**
     * Consulta un grupo.
     *
     * <p>Endpoint: {@code GET /api/grupos/{id}}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del grupo
     * @return los datos del grupo
     */
    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public GrupoResponse buscar(@PathVariable Integer id) {
        return aResponse(grupoService.buscarPorId(id));
    }

    /**
     * Consulta los cupos de un grupo.
     *
     * <p>Endpoint: {@code GET /api/grupos/{id}/cupos}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del grupo
     * @return el cupo máximo y los cupos disponibles
     */
    @GetMapping("/{id}/cupos")
    @PreAuthorize(Permisos.AUTENTICADO)
    public CuposResponse cupos(@PathVariable Integer id) {
        Grupo grupo = grupoService.buscarPorId(id);
        return new CuposResponse(id, grupo.getCupoMaximo(), grupoService.calcularCuposDisponibles(id));
    }

    /**
     * Crea un grupo.
     *
     * <p>Endpoint: {@code POST /api/grupos}. Acceso: director de programa o administrador.
     *
     * @param request los datos validados del grupo
     * @return la respuesta 201 con el grupo creado
     */
    @PostMapping
    @PreAuthorize(Permisos.DIRECCION)
    public ResponseEntity<GrupoResponse> crear(@Valid @RequestBody GrupoRequest request) {
        Grupo grupo = grupoService.crearGrupo(request.idPeriodo(), request.idMateria(), request.codigo(), request.cupoMaximo());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(grupo));
    }

    /**
     * Asigna un docente al grupo.
     *
     * <p>Endpoint: {@code PUT /api/grupos/{id}/docente/{idDocente}}. Acceso: director de programa o
     * administrador.
     *
     * @param id el identificador del grupo
     * @param idDocente el identificador del docente
     * @return los datos actualizados
     */
    @PutMapping("/{id}/docente/{idDocente}")
    @PreAuthorize(Permisos.DIRECCION)
    public GrupoResponse asignarDocente(@PathVariable Integer id, @PathVariable Integer idDocente) {
        return aResponse(grupoService.asignarDocente(id, idDocente));
    }

    /**
     * Agrega una sesión al grupo.
     *
     * <p>Endpoint: {@code POST /api/grupos/{id}/sesiones}. Acceso: director de programa o administrador.
     *
     * @param id el identificador del grupo
     * @param request los datos validados de la sesión
     * @return la respuesta 201 con el grupo actualizado
     */
    @PostMapping("/{id}/sesiones")
    @PreAuthorize(Permisos.DIRECCION)
    public ResponseEntity<GrupoResponse> agregarSesion(@PathVariable Integer id, @Valid @RequestBody SesionRequest request) {
        Grupo grupo = grupoService.agregarSesion(id, request.franja().aFranja(), request.idAula(), request.usoExcepcional());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(grupo));
    }


    /**
     * Mueve una sesión del grupo.
     *
     * <p>Endpoint: {@code PUT /api/grupos/{id}/sesiones/{idSesion}}. Acceso: director de programa o
     * administrador.
     *
     * @param id el identificador del grupo
     * @param idSesion el identificador de la sesión del grupo
     * @param request los datos validados de la sesión
     * @return los datos actualizados
     */
    @PutMapping("/{id}/sesiones/{idSesion}")
    @PreAuthorize(Permisos.DIRECCION)
    public GrupoResponse moverSesion(@PathVariable Integer id, @PathVariable Integer idSesion,
                                     @Valid @RequestBody SesionRequest request) {
        return aResponse(grupoService.moverSesion(id, idSesion, request.franja().aFranja(), request.idAula(),
                request.usoExcepcional()));
    }

    /**
     * Quita una sesión del grupo.
     *
     * <p>Endpoint: {@code DELETE /api/grupos/{id}/sesiones/{idSesion}}. Acceso: director de programa o
     * administrador.
     *
     * @param id el identificador del grupo
     * @param idSesion el identificador de la sesión del grupo
     * @return los datos actualizados
     */
    @DeleteMapping("/{id}/sesiones/{idSesion}")
    @PreAuthorize(Permisos.DIRECCION)
    public GrupoResponse quitarSesion(@PathVariable Integer id, @PathVariable Integer idSesion) {
        return aResponse(grupoService.quitarSesion(id, idSesion));
    }


    /**
     * Consulta las advertencias por días consecutivos; la franja adicional solo se evalúa si llegan sus tres
     * datos.
     *
     * <p>Endpoint: {@code GET /api/grupos/{id}/advertencias}. Acceso: director de programa o administrador.
     *
     * @param id el identificador del grupo
     * @param dia el día de la franja adicional, o {@code null}
     * @param horaInicio la hora de inicio, o {@code null}
     * @param horaFin la hora de fin, o {@code null}
     * @return los mensajes de advertencia
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

    /**
     * Sugiere aulas para el grupo.
     *
     * <p>Endpoint: {@code GET /api/grupos/{id}/aulas-sugeridas}. Acceso: director de programa o
     * administrador.
     *
     * @param id el identificador del grupo
     * @return las aulas candidatas
     */
    @GetMapping("/{id}/aulas-sugeridas")
    @PreAuthorize(Permisos.DIRECCION)
    public List<AulaResponse> aulasSugeridas(@PathVariable Integer id) {
        return grupoService.sugerirAulas(id).stream().map(AcademicoMapper::aResponse).toList();
    }

    /**
     * Cierra el grupo.
     *
     * <p>Endpoint: {@code POST /api/grupos/{id}/cerrar}. Acceso: director de programa o administrador.
     *
     * @param id el identificador del grupo
     * @return los datos actualizados
     */
    @PostMapping("/{id}/cerrar")
    @PreAuthorize(Permisos.DIRECCION)
    public GrupoResponse cerrar(@PathVariable Integer id) {
        return aResponse(grupoService.cerrarGrupo(id));
    }


    /**
     * Reasigna los estudiantes del grupo cerrado a otro grupo.
     *
     * <p>Endpoint: {@code POST /api/grupos/{id}/reasignar/{idDestino}}. Acceso: director de programa o
     * administrador.
     *
     * @param id el identificador del grupo
     * @param idDestino el identificador del grupo destino
     * @return el resultado de la reasignación
     */
    @PostMapping("/{id}/reasignar/{idDestino}")
    @PreAuthorize(Permisos.DIRECCION)
    public ResultadoReasignacion reasignar(@PathVariable Integer id, @PathVariable Integer idDestino) {
        return inscripcionMateriaService.reasignarEstudiantes(id, idDestino);
    }

    /**
     * Registra una observación del docente autenticado.
     *
     * <p>Endpoint: {@code POST /api/grupos/{id}/observaciones}. Acceso: docente.
     *
     * @param id el identificador del grupo
     * @param request el texto de la observación
     * @return la respuesta 201 con la observación creada
     */
    @PostMapping("/{id}/observaciones")
    @PreAuthorize(Permisos.DOCENTE)
    public ResponseEntity<ObservacionResponse> registrarObservacion(@PathVariable Integer id,
                                                                    @Valid @RequestBody ObservacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(HorarioMapper.aResponse(
                observacionAsignacionService.registrar(id, usuarioActual.obtenerIdUsuario(), request.texto())));
    }

    /**
     * Marca una observación como resuelta.
     *
     * <p>Endpoint: {@code PATCH /api/grupos/observaciones/{idObservacion}/resolver}. Acceso: director de
     * programa o administrador.
     *
     * @param idObservacion el identificador de la observación
     * @return los datos actualizados
     */
    @PatchMapping("/observaciones/{idObservacion}/resolver")
    @PreAuthorize(Permisos.DIRECCION)
    public ObservacionResponse resolverObservacion(@PathVariable Integer idObservacion) {
        return HorarioMapper.aResponse(observacionAsignacionService.resolver(idObservacion));
    }

    /**
     * Convierte un grupo en su respuesta, con sus cupos disponibles.
     *
     * @param grupo el grupo
     * @return la respuesta correspondiente
     */
    private GrupoResponse aResponse(Grupo grupo) {
        return HorarioMapper.aResponse(grupo, grupoService.calcularCuposDisponibles(grupo.getIdGrupo()));
    }
}
