package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.mapper.AcademicoMapper;
import co.edu.unbosque.collegeAdministrator.dto.request.AulaRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.AulaResponse;
import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import co.edu.unbosque.collegeAdministrator.service.api.AulaServiceAPI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;


/**
 * Controlador REST de las aulas.
 */
@RestController
@RequestMapping("/api/aulas")
public class AulaController {

    /** El servicio de gestión de aulas. */
    private final AulaServiceAPI aulaService;

    /**
     * Crea una instancia de {@code AulaController} con las dependencias requeridas.
     *
     * @param aulaService el servicio de gestión de aulas
     */
    public AulaController(AulaServiceAPI aulaService) {
        this.aulaService = aulaService;
    }


    /**
     * Lista las aulas activas, con filtros opcionales.
     *
     * <p>Endpoint: {@code GET /api/aulas}. Acceso: cualquier usuario autenticado.
     *
     * @param bloque el bloque por el que se filtra, o {@code null}
     * @param tipo el tipo de espacio por el que se filtra, o {@code null}
     * @return las aulas que cumplen los filtros
     */
    @GetMapping
    @PreAuthorize(Permisos.AUTENTICADO)
    public List<AulaResponse> listar(@RequestParam(required = false) Bloque bloque,
                                     @RequestParam(required = false) TipoEspacio tipo) {
        List<Aula> aulas;
        if (bloque != null && tipo != null) {
            aulas = aulaService.listarPorBloqueYTipo(bloque, tipo);
        } else if (bloque != null) {
            aulas = aulaService.listarPorBloque(bloque);
        } else {
            aulas = aulaService.listarActivas().stream()
                    .filter(aula -> tipo == null || aula.getTipoEspacio() == tipo)
                    .toList();
        }
        return aulas.stream().map(AcademicoMapper::aResponse).toList();
    }

    /**
     * Consulta un aula.
     *
     * <p>Endpoint: {@code GET /api/aulas/{id}}. Acceso: cualquier usuario autenticado.
     *
     * @param id el identificador del aula
     * @return los datos del aula
     */
    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public AulaResponse buscar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(aulaService.buscarPorId(id));
    }

    /**
     * Crea un aula.
     *
     * <p>Endpoint: {@code POST /api/aulas}. Acceso: administrador.
     *
     * @param request los datos validados del aula
     * @return la respuesta 201 con el aula creada
     */
    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<AulaResponse> crear(@Valid @RequestBody AulaRequest request) {
        Aula aula = aulaService.guardar(AcademicoMapper.nuevaAula(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AcademicoMapper.aResponse(aula));
    }

    /**
     * Actualiza un aula.
     *
     * <p>Endpoint: {@code PUT /api/aulas/{id}}. Acceso: administrador.
     *
     * @param id el identificador del aula
     * @param request los datos validados del aula
     * @return los datos actualizados
     */
    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public AulaResponse actualizar(@PathVariable Integer id, @Valid @RequestBody AulaRequest request) {
        Aula aula = aulaService.buscarPorId(id);
        AcademicoMapper.aplicar(request, aula);
        return AcademicoMapper.aResponse(aulaService.guardar(aula));
    }

    /**
     * Desactiva un aula.
     *
     * <p>Endpoint: {@code PATCH /api/aulas/{id}/desactivar}. Acceso: administrador.
     *
     * @param id el identificador del aula
     * @return los datos actualizados
     */
    @PatchMapping("/{id}/desactivar")
    @PreAuthorize(Permisos.ADMIN)
    public AulaResponse desactivar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(aulaService.desactivar(id));
    }


    /**
     * Sugiere aulas según su capacidad y características.
     *
     * <p>Endpoint: {@code GET /api/aulas/sugerencias}. Acceso: director de programa o administrador.
     *
     * @param capacidad la capacidad mínima
     * @param caracteristicas las características exigidas, o {@code null}
     * @param sede la sede de búsqueda, o {@code null}
     * @return las aulas candidatas
     */
    @GetMapping("/sugerencias")
    @PreAuthorize(Permisos.DIRECCION)
    public List<AulaResponse> sugerencias(@RequestParam Integer capacidad,
                                          @RequestParam(required = false) Set<CaracteristicaAula> caracteristicas,
                                          @RequestParam(required = false) Sede sede) {
        return aulaService.sugerirAulas(capacidad, caracteristicas, sede).stream()
                .map(AcademicoMapper::aResponse)
                .toList();
    }
}
