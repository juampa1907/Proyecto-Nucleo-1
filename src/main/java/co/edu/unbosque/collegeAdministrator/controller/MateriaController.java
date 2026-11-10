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
 * Controlador REST de las materias y sus requisitos.
 */
@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    /** El servicio de gestión de materias. */
    private final MateriaServiceAPI materiaService;

    /**
     * Crea una instancia de {@code MateriaController} con las dependencias requeridas.
     *
     * @param materiaService el servicio de gestión de materias
     */
    public MateriaController(MateriaServiceAPI materiaService) {
        this.materiaService = materiaService;
    }


    /**
     * Lista las materias, opcionalmente por nombre.
     *
     * <p>Endpoint: {@code GET /api/materias}. Acceso: cualquier usuario autenticado.
     *
     * @param nombre el texto buscado, o {@code null} o vacío para todas
     * @return las materias encontradas
     */
    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<MateriaResponse> listar(@RequestParam(required = false) String nombre) {
        List<Materia> materias = nombre == null || nombre.isBlank()
                ? materiaService.listar()
                : materiaService.buscarPorNombre(nombre);
        return materias.stream().map(AcademicoMapper::aResponse).toList();
    }

    /**
     * Consulta una materia.
     *
     * <p>Endpoint: {@code GET /api/materias/{id}}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador de la materia
     * @return los datos de la materia
     */
    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public MateriaResponse buscar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(materiaService.buscarPorId(id));
    }

    /**
     * Crea una materia.
     *
     * <p>Endpoint: {@code POST /api/materias}. Acceso: administrador.
     *
     * @param request los datos validados de la materia
     * @return la respuesta 201 con la materia creada
     */
    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<MateriaResponse> crear(@Valid @RequestBody MateriaRequest request) {
        Materia materia = materiaService.guardar(AcademicoMapper.nuevaMateria(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AcademicoMapper.aResponse(materia));
    }

    /**
     * Actualiza una materia.
     *
     * <p>Endpoint: {@code PUT /api/materias/{id}}. Acceso: administrador.
     *
     * @param id el identificador de la materia
     * @param request los datos validados de la materia
     * @return los datos actualizados
     */
    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse actualizar(@PathVariable Integer id, @Valid @RequestBody MateriaRequest request) {
        Materia materia = materiaService.buscarPorId(id);
        AcademicoMapper.aplicar(request, materia);
        return AcademicoMapper.aResponse(materiaService.guardar(materia));
    }

    /**
     * Elimina una materia.
     *
     * <p>Endpoint: {@code DELETE /api/materias/{id}}. Acceso: administrador.
     *
     * @param id el identificador de la materia
     * @return la respuesta 204
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        materiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Agrega un prerrequisito.
     *
     * <p>Endpoint: {@code POST /api/materias/{id}/prerrequisitos/{idPrerrequisito}}. Acceso: administrador.
     *
     * @param id el identificador de la materia
     * @param idPrerrequisito el identificador de la materia prerrequisito
     * @return los datos actualizados
     */
    @PostMapping("/{id}/prerrequisitos/{idPrerrequisito}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse agregarPrerrequisito(@PathVariable Integer id, @PathVariable Integer idPrerrequisito) {
        return AcademicoMapper.aResponse(materiaService.agregarPrerrequisito(id, idPrerrequisito));
    }

    /**
     * Quita un prerrequisito.
     *
     * <p>Endpoint: {@code DELETE /api/materias/{id}/prerrequisitos/{idPrerrequisito}}. Acceso: administrador.
     *
     * @param id el identificador de la materia
     * @param idPrerrequisito el identificador de la materia prerrequisito
     * @return los datos actualizados
     */
    @DeleteMapping("/{id}/prerrequisitos/{idPrerrequisito}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse quitarPrerrequisito(@PathVariable Integer id, @PathVariable Integer idPrerrequisito) {
        return AcademicoMapper.aResponse(materiaService.quitarPrerrequisito(id, idPrerrequisito));
    }

    /**
     * Agrega un correquisito.
     *
     * <p>Endpoint: {@code POST /api/materias/{id}/correquisitos/{idCorrequisito}}. Acceso: administrador.
     *
     * @param id el identificador de la materia
     * @param idCorrequisito el identificador de la materia correquisito
     * @return los datos actualizados
     */
    @PostMapping("/{id}/correquisitos/{idCorrequisito}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse agregarCorrequisito(@PathVariable Integer id, @PathVariable Integer idCorrequisito) {
        return AcademicoMapper.aResponse(materiaService.agregarCorrequisito(id, idCorrequisito));
    }

    /**
     * Quita un correquisito.
     *
     * <p>Endpoint: {@code DELETE /api/materias/{id}/correquisitos/{idCorrequisito}}. Acceso: administrador.
     *
     * @param id el identificador de la materia
     * @param idCorrequisito el identificador de la materia correquisito
     * @return los datos actualizados
     */
    @DeleteMapping("/{id}/correquisitos/{idCorrequisito}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse quitarCorrequisito(@PathVariable Integer id, @PathVariable Integer idCorrequisito) {
        return AcademicoMapper.aResponse(materiaService.quitarCorrequisito(id, idCorrequisito));
    }

    /**
     * Agrega un requisito de aula.
     *
     * <p>Endpoint: {@code POST /api/materias/{id}/requisitos-aula/{caracteristica}}. Acceso: administrador.
     *
     * @param id el identificador de la materia
     * @param caracteristica la característica de aula
     * @return los datos actualizados
     */
    @PostMapping("/{id}/requisitos-aula/{caracteristica}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse agregarRequisitoAula(@PathVariable Integer id, @PathVariable CaracteristicaAula caracteristica) {
        return AcademicoMapper.aResponse(materiaService.agregarRequisitoAula(id, caracteristica));
    }

    /**
     * Quita un requisito de aula.
     *
     * <p>Endpoint: {@code DELETE /api/materias/{id}/requisitos-aula/{caracteristica}}. Acceso: administrador.
     *
     * @param id el identificador de la materia
     * @param caracteristica la característica de aula
     * @return los datos actualizados
     */
    @DeleteMapping("/{id}/requisitos-aula/{caracteristica}")
    @PreAuthorize(Permisos.ADMIN)
    public MateriaResponse quitarRequisitoAula(@PathVariable Integer id, @PathVariable CaracteristicaAula caracteristica) {
        return AcademicoMapper.aResponse(materiaService.quitarRequisitoAula(id, caracteristica));
    }
}
