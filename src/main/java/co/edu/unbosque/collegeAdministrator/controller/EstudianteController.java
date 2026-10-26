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


@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteServiceAPI estudianteService;
    private final InscripcionMateriaServiceAPI inscripcionMateriaService;
    private final GrupoServiceAPI grupoService;
    private final ComprobanteServiceAPI comprobanteService;
    private final UsuarioActual usuarioActual;

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

    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<EstudianteResponse> crear(@Valid @RequestBody EstudianteRequest request) {
        Estudiante estudiante = estudianteService.guardar(UsuarioMapper.nuevoEstudiante(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.aResponse(estudiante));
    }

    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public EstudianteResponse actualizar(@PathVariable Integer id, @Valid @RequestBody EstudianteActualizacionRequest request) {
        Estudiante estudiante = estudianteService.buscarPorId(id);
        estudiante.setCorreo(request.correo());
        estudiante.setNumeroSemestre(request.numeroSemestre());
        return UsuarioMapper.aResponse(estudianteService.guardar(estudiante));
    }

    @GetMapping
    @PreAuthorize(Permisos.DIRECCION)
    public List<EstudianteResponse> listar() {
        return estudianteService.listar().stream().map(UsuarioMapper::aResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public EstudianteResponse buscar(@PathVariable Integer id) {
        verificarAcceso(id);
        return UsuarioMapper.aResponse(estudianteService.buscarPorId(id));
    }


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

    @GetMapping("/{id}/materias-actuales")
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<MateriaResumen> materiasActuales(@PathVariable Integer id) {
        verificarAcceso(id);
        return estudianteService.listarMateriasActuales(id).stream().map(AcademicoMapper::aResumen).toList();
    }


    @GetMapping("/{id}/horario")
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<GrupoResponse> horario(@PathVariable Integer id, @RequestParam Integer idPeriodo) {
        verificarAcceso(id);
        return inscripcionMateriaService.listarCargaDelPeriodo(id, idPeriodo).stream()
                .map(InscripcionMateria::getGrupo)
                .map(grupo -> HorarioMapper.aResponse(grupo, grupoService.calcularCuposDisponibles(grupo.getIdGrupo())))
                .toList();
    }


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

    private void verificarAcceso(Integer idEstudiante) {
        usuarioActual.verificarPropietarioOAlgunRol(idEstudiante, Rol.ADMINISTRADOR, Rol.DIRECTOR_PROGRAMA);
    }
}
