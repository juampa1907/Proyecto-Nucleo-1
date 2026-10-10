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
 * HU-31 a HU-38. El estudiante se inscribe a sí mismo (el id sale del token, no del cuerpo).
 */
@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionMateriaServiceAPI inscripcionMateriaService;
    private final UsuarioActual usuarioActual;

    public InscripcionController(InscripcionMateriaServiceAPI inscripcionMateriaService, UsuarioActual usuarioActual) {
        this.inscripcionMateriaService = inscripcionMateriaService;
        this.usuarioActual = usuarioActual;
    }

    @PostMapping
    @PreAuthorize(Permisos.ESTUDIANTE)
    public ResponseEntity<InscripcionResponse> inscribir(@Valid @RequestBody InscripcionRequest request) {
        InscripcionMateria inscripcion = inscripcionMateriaService.inscribir(usuarioActual.obtenerIdUsuario(), request.idGrupo());
        return ResponseEntity.status(HttpStatus.CREATED).body(InscripcionMapper.aResponse(inscripcion));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Permisos.ESTUDIANTE_O_ADMIN)
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        InscripcionMateria inscripcion = inscripcionMateriaService.buscarPorId(id);
        usuarioActual.verificarPropietarioOAdmin(inscripcion.getEstudiante().getIdUsuario());
        inscripcionMateriaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    // Solo el docente del grupo (o el administrador) registra la nota.
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
    @PostMapping("/matricula")
    @PreAuthorize(Permisos.ESTUDIANTE)
    public MatriculaResponse confirmarMatricula(@RequestParam Integer idPeriodo) {
        return InscripcionMapper.aResponse(
                inscripcionMateriaService.confirmarMatricula(usuarioActual.obtenerIdUsuario(), idPeriodo));
    }
}
