package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.AcademicoMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.MateriaRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.MateriaResponse;
import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.service.api.MateriaServiceAPI;
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
 * HU-11, HU-13, HU-15: catálogo de materias, requisitos de aula, prerrequisitos y correquisitos.
 */
@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private final MateriaServiceAPI materiaService;

    public MateriaController(MateriaServiceAPI materiaService) {
        this.materiaService = materiaService;
    }

    /**
     * @param nombre opcional: filtra por nombre (contiene, sin distinguir mayúsculas).
     */
    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<MateriaResponse> listar(@RequestParam(required = false) String nombre) {
        List<Materia> materias = nombre == null || nombre.isBlank()
                ? materiaService.listar()
                : materiaService.buscarPorNombre(nombre);
        return materias.stream().map(AcademicoMapper::aResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public MateriaResponse buscar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(materiaService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<MateriaResponse> crear(@Valid @RequestBody MateriaRequest request) {
        Materia materia = materiaService.guardar(AcademicoMapper.nuevaMateria(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AcademicoMapper.aResponse(materia));
    }

    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse actualizar(@PathVariable Integer id, @Valid @RequestBody MateriaRequest request) {
        Materia materia = materiaService.buscarPorId(id);
        AcademicoMapper.aplicar(request, materia);
        return AcademicoMapper.aResponse(materiaService.guardar(materia));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        materiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/prerrequisitos/{idPrerrequisito}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse agregarPrerrequisito(@PathVariable Integer id, @PathVariable Integer idPrerrequisito) {
        return AcademicoMapper.aResponse(materiaService.agregarPrerrequisito(id, idPrerrequisito));
    }

    @DeleteMapping("/{id}/prerrequisitos/{idPrerrequisito}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse quitarPrerrequisito(@PathVariable Integer id, @PathVariable Integer idPrerrequisito) {
        return AcademicoMapper.aResponse(materiaService.quitarPrerrequisito(id, idPrerrequisito));
    }

    @PostMapping("/{id}/correquisitos/{idCorrequisito}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse agregarCorrequisito(@PathVariable Integer id, @PathVariable Integer idCorrequisito) {
        return AcademicoMapper.aResponse(materiaService.agregarCorrequisito(id, idCorrequisito));
    }

    @DeleteMapping("/{id}/correquisitos/{idCorrequisito}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse quitarCorrequisito(@PathVariable Integer id, @PathVariable Integer idCorrequisito) {
        return AcademicoMapper.aResponse(materiaService.quitarCorrequisito(id, idCorrequisito));
    }

    @PostMapping("/{id}/requisitos-aula/{caracteristica}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse agregarRequisitoAula(@PathVariable Integer id, @PathVariable CaracteristicaAula caracteristica) {
        return AcademicoMapper.aResponse(materiaService.agregarRequisitoAula(id, caracteristica));
    }

    @DeleteMapping("/{id}/requisitos-aula/{caracteristica}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse quitarRequisitoAula(@PathVariable Integer id, @PathVariable CaracteristicaAula caracteristica) {
        return AcademicoMapper.aResponse(materiaService.quitarRequisitoAula(id, caracteristica));
    }
}
