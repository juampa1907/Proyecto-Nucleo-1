package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.AcademicoMapper;
import co.edu.unbosque.collegeAdministrator.dto.mapper.HorarioMapper;
import co.edu.unbosque.collegeAdministrator.dto.mapper.InscripcionMapper;
import co.edu.unbosque.collegeAdministrator.dto.mapper.UsuarioMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.EstudianteActualizacionRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.EstudianteRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.EstudianteResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.GrupoResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.InscripcionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.MateriaResumen;
import co.edu.unbosque.collegeAdministrator.entity.Estudiante;
import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import co.edu.unbosque.collegeAdministrator.security.UsuarioActual;
import co.edu.unbosque.collegeAdministrator.service.api.ComprobanteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.EstudianteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.GrupoServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.InscripcionMateriaServiceAPI;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * Controlador REST de los estudiantes.
 */
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    /** El servicio de gestión de estudiantes. */
    private final EstudianteServiceAPI estudianteService;
    /** El servicio de inscripción de materias. */
    private final InscripcionMateriaServiceAPI inscripcionMateriaService;
    /** El servicio de gestión de grupos y sesiones. */
    private final GrupoServiceAPI grupoService;
    /** El servicio que genera los comprobantes de carga académica. */
    private final ComprobanteServiceAPI comprobanteService;
    /** El componente que da acceso al usuario autenticado. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea una instancia de {@code EstudianteController} con las dependencias requeridas.
     *
     * @param estudianteService el servicio de gestión de estudiantes
     * @param inscripcionMateriaService el servicio de inscripción de materias
     * @param grupoService el servicio de gestión de grupos y sesiones
     * @param comprobanteService el servicio que genera los comprobantes de carga académica
     * @param usuarioActual el componente que da acceso al usuario autenticado
     */
    public EstudianteController(EstudianteServiceAPI estudianteService,
                                InscripcionMateriaServiceAPI inscripcionMateriaService,
                                GrupoServiceAPI grupoService,
                                ComprobanteServiceAPI comprobanteService,
                                UsuarioActual usuarioActual) {
        this.estudianteService = estudianteService;
        this.inscripcionMateriaService = inscripcionMateriaService;
        this.grupoService = grupoService;
        this.comprobanteService = comprobanteService;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Crea un estudiante.
     *
     * <p>Endpoint: {@code POST /api/estudiantes}. Acceso: administrador.
     *
     * @param request los datos validados del estudiante
     * @return la respuesta 201 con el estudiante creado
     */
    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<EstudianteResponse> crear(@Valid @RequestBody EstudianteRequest request) {
        Estudiante estudiante = estudianteService.guardar(UsuarioMapper.nuevoEstudiante(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.aResponse(estudiante));
    }

    /**
     * Actualiza un estudiante.
     *
     * <p>Endpoint: {@code PUT /api/estudiantes/{id}}. Acceso: administrador.
     *
     * @param id el identificador del estudiante
     * @param request los datos validados
     * @return los datos actualizados
     */
    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public EstudianteResponse actualizar(@PathVariable Integer id, @Valid @RequestBody EstudianteActualizacionRequest request) {
        Estudiante estudiante = estudianteService.buscarPorId(id);
        estudiante.setCorreo(request.correo());
        estudiante.setNumeroSemestre(request.numeroSemestre());
        return UsuarioMapper.aResponse(estudianteService.guardar(estudiante));
    }

    /**
     * Lista los estudiantes.
     *
     * <p>Endpoint: {@code GET /api/estudiantes}. Acceso: director de programa o administrador.
     *
     * @return todos los estudiantes
     */
    @GetMapping
    @PreAuthorize(Permisos.DIRECCION)
    public List<EstudianteResponse> listar() {
        return estudianteService.listar().stream().map(UsuarioMapper::aResponse).toList();
    }

    /**
     * Consulta un estudiante, si el solicitante tiene acceso.
     *
     * <p>Endpoint: {@code GET /api/estudiantes/{id}}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del estudiante
     * @return los datos del estudiante
     */
    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public EstudianteResponse buscar(@PathVariable Integer id) {
        verificarAcceso(id);
        return UsuarioMapper.aResponse(estudianteService.buscarPorId(id));
    }


    /**
     * Consulta las inscripciones del estudiante.
     *
     * <p>Endpoint: {@code GET /api/estudiantes/{id}/inscripciones}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del estudiante
     * @param estado el estado por el que se filtra, o {@code null} para todas
     * @return las inscripciones encontradas
     */
    @GetMapping("/{id}/inscripciones")
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<InscripcionResponse> inscripciones(@PathVariable Integer id,
                                                   @RequestParam(required = false) EstadoMateria estado) {
        verificarAcceso(id);
        List<InscripcionMateria> inscripciones = estado == null
                ? estudianteService.buscarPorId(id).getHistorialInscripciones()
                : inscripcionMateriaService.listarPorEstudianteYEstado(id, estado);
        return inscripciones.stream().map(InscripcionMapper::aResponse).toList();
    }

    /**
     * Consulta las materias en curso del estudiante.
     *
     * <p>Endpoint: {@code GET /api/estudiantes/{id}/materias-actuales}. Acceso: cualquier usuario
     * autenticado.
     *
     * @param id el identificador del estudiante
     * @return las materias en curso
     */
    @GetMapping("/{id}/materias-actuales")
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<MateriaResumen> materiasActuales(@PathVariable Integer id) {
        verificarAcceso(id);
        return estudianteService.listarMateriasActuales(id).stream().map(AcademicoMapper::aResumen).toList();
    }


    /**
     * Consulta el horario del estudiante en un periodo.
     *
     * <p>Endpoint: {@code GET /api/estudiantes/{id}/horario}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del estudiante
     * @param idPeriodo el identificador del periodo académico
     * @return los grupos de su carga académica
     */
    @GetMapping("/{id}/horario")
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<GrupoResponse> horario(@PathVariable Integer id, @RequestParam Integer idPeriodo) {
        verificarAcceso(id);
        return inscripcionMateriaService.listarCargaDelPeriodo(id, idPeriodo).stream()
                .map(InscripcionMateria::getGrupo)
                .map(grupo -> HorarioMapper.aResponse(grupo, grupoService.calcularCuposDisponibles(grupo.getIdGrupo())))
                .toList();
    }


    /**
     * Descarga el comprobante de carga académica del estudiante.
     *
     * <p>Endpoint: {@code GET /api/estudiantes/{id}/comprobante}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del estudiante
     * @param idPeriodo el identificador del periodo académico
     * @return la respuesta con el archivo PDF
     */
    @GetMapping("/{id}/comprobante")
    @PreAuthorize(Permisos.AUTENTICADO)
    public ResponseEntity<byte[]> comprobante(@PathVariable Integer id, @RequestParam Integer idPeriodo) {
        verificarAcceso(id);
        byte[] pdf = comprobanteService.generarComprobante(id, idPeriodo);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename("comprobante-periodo-" + idPeriodo + ".pdf").build().toString())
                .body(pdf);
    }

    /**
     * Permite el acceso al propio estudiante, a administradores y directores.
     *
     * @param idEstudiante el identificador del estudiante consultado
     */
    private void verificarAcceso(Integer idEstudiante) {
        usuarioActual.verificarPropietarioOAlgunRol(idEstudiante, Rol.ADMINISTRADOR, Rol.DIRECTOR_PROGRAMA);
    }
}
