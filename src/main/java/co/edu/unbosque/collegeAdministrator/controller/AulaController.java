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


@RestController
@RequestMapping("/api/aulas")
public class AulaController {

    private final AulaServiceAPI aulaService;

    public AulaController(AulaServiceAPI aulaService) {
        this.aulaService = aulaService;
    }


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

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.AUTENTICADO)
    public AulaResponse buscar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(aulaService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize(Permisos.ADMIN)
    public ResponseEntity<AulaResponse> crear(@Valid @RequestBody AulaRequest request) {
        Aula aula = aulaService.guardar(AcademicoMapper.nuevaAula(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AcademicoMapper.aResponse(aula));
    }

    @PutMapping("/{id}")
    @PreAuthorize(Permisos.ADMIN)
    public AulaResponse actualizar(@PathVariable Integer id, @Valid @RequestBody AulaRequest request) {
        Aula aula = aulaService.buscarPorId(id);
        AcademicoMapper.aplicar(request, aula);
        return AcademicoMapper.aResponse(aulaService.guardar(aula));
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize(Permisos.ADMIN)
    public AulaResponse desactivar(@PathVariable Integer id) {
        return AcademicoMapper.aResponse(aulaService.desactivar(id));
    }


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
