package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.InscripcionMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.InscripcionRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.NotaRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.InscripcionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.MatriculaResponse;
import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.security.UsuarioActual;
import co.edu.unbosque.collegeAdministrator.service.api.InscripcionMateriaServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.exception.AccesoDenegadoException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST de las inscripciones y la matrícula.
 */
@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    /** El servicio de inscripción de materias. */
    private final InscripcionMateriaServiceAPI inscripcionMateriaService;
    /** El componente que da acceso al usuario autenticado. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea una instancia de {@code InscripcionController} con las dependencias requeridas.
     *
     * @param inscripcionMateriaService el servicio de inscripción de materias
     * @param usuarioActual el componente que da acceso al usuario autenticado
     */
    public InscripcionController(InscripcionMateriaServiceAPI inscripcionMateriaService, UsuarioActual usuarioActual) {
        this.inscripcionMateriaService = inscripcionMateriaService;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Inscribe al estudiante autenticado en un grupo.
     *
     * <p>Endpoint: {@code POST /api/inscripciones}. Acceso: estudiante.
     *
     * @param request el grupo elegido
     * @return la respuesta 201 con la inscripción creada
     */
    @PostMapping
    @PreAuthorize(Permisos.ESTUDIANTE)
    public ResponseEntity<InscripcionResponse> inscribir(@Valid @RequestBody InscripcionRequest request) {
        InscripcionMateria inscripcion = inscripcionMateriaService.inscribir(usuarioActual.obtenerIdUsuario(), request.idGrupo());
        return ResponseEntity.status(HttpStatus.CREATED).body(InscripcionMapper.aResponse(inscripcion));
    }

    /**
     * Cancela una inscripción propia o, para un administrador, de cualquier estudiante.
     *
     * <p>Endpoint: {@code DELETE /api/inscripciones/{id}}. Acceso: estudiante o administrador.
     *
     * @param id el identificador de la inscripción
     * @return la respuesta 204
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(Permisos.ESTUDIANTE_O_ADMIN)
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        InscripcionMateria inscripcion = inscripcionMateriaService.buscarPorId(id);
        usuarioActual.verificarPropietarioOAdmin(inscripcion.getEstudiante().getIdUsuario());
        inscripcionMateriaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Registra una nota; solo el docente del grupo o un administrador.
     *
     * <p>Endpoint: {@code PUT /api/inscripciones/{id}/nota}. Acceso: docente o administrador.
     *
     * @param id el identificador de la inscripción
     * @param request la nota validada
     * @return la inscripción actualizada
     * @throws AccesoDenegadoException si el docente autenticado no es el del grupo
     */
    @PutMapping("/{id}/nota")
    @PreAuthorize(Permisos.DOCENTE_O_ADMIN)
    public InscripcionResponse registrarNota(@PathVariable Integer id, @Valid @RequestBody NotaRequest request) {
        InscripcionMateria inscripcion = inscripcionMateriaService.buscarPorId(id);
        if (!usuarioActual.esAdministrador()) {
            Docente docente = inscripcion.getGrupo().getDocente();
            if (docente == null || !docente.getIdUsuario().equals(usuarioActual.obtenerIdUsuario())) {
                throw new AccesoDenegadoException("Solo el docente del grupo puede registrar la nota");
            }
        }
        return InscripcionMapper.aResponse(inscripcionMateriaService.registrarNota(id, request.nota()));
    }

    // HU-38: el estudiante confirma su matrícula del periodo (valida créditos mínimos y máximos).
    /**
     * Confirma la matrícula del estudiante autenticado (HU-38).
     *
     * <p>Endpoint: {@code POST /api/inscripciones/matricula}. Acceso: estudiante.
     *
     * @param idPeriodo el identificador del periodo académico
     * @return la matrícula confirmada
     */
    @PostMapping("/matricula")
    @PreAuthorize(Permisos.ESTUDIANTE)
    public MatriculaResponse confirmarMatricula(@RequestParam Integer idPeriodo) {
        return InscripcionMapper.aResponse(
                inscripcionMateriaService.confirmarMatricula(usuarioActual.obtenerIdUsuario(), idPeriodo));
    }
}
