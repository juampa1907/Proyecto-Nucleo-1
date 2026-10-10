package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import co.edu.unbosque.collegeAdministrator.repository.AulaRepository;
import co.edu.unbosque.collegeAdministrator.repository.LimiteInventarioAulaRepository;
import co.edu.unbosque.collegeAdministrator.service.api.AulaServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AulaServiceImpl extends GenericServiceImpl<Aula, Integer> implements AulaServiceAPI {

    private final AulaRepository aulaRepository;
    private final LimiteInventarioAulaRepository limiteInventarioAulaRepository;

    public AulaServiceImpl(AulaRepository aulaRepository, LimiteInventarioAulaRepository limiteInventarioAulaRepository) {
        this.aulaRepository = aulaRepository;
        this.limiteInventarioAulaRepository = limiteInventarioAulaRepository;
    }

    @Override
    protected JpaRepository<Aula, Integer> getRepository() {
        return aulaRepository;
    }

    @Override
    public Aula guardar(Aula aula) {
        if (aula.getCapacidad() == null || aula.getCapacidad() <= 0) {
            throw new ReglaNegocioException("La capacidad del aula debe ser mayor a 0");
        }
        if (aula.getBloque() == null) {
            throw new ReglaNegocioException("El bloque del aula es obligatorio");
        }
        // ESP-F-03: las aulas de cómputo existen solo en la sede Usaquén.
        boolean esDeComputo = aula.getTipoEspacio() == TipoEspacio.COMPUTO
                || aula.tieneCaracteristica(CaracteristicaAula.COMPUTADORES);
        if (esDeComputo && aula.getBloque().getSede() != Sede.USAQUEN) {
            throw new ReglaNegocioException("Las aulas de cómputo solo pueden registrarse en la sede Usaquén");
        }
        if (aula.getIdAula() == null) {
            validarTopeInventario(aula);
        }
        return super.guardar(aula);
    }

    // ESP-F-02 / ESP-F-04: no se registran más aulas de un tipo que las del inventario real de la sede.
    private void validarTopeInventario(Aula aula) {
        Sede sede = aula.getBloque().getSede();
        limiteInventarioAulaRepository.findByTipoEspacioAndSede(aula.getTipoEspacio(), sede).ifPresent(limite -> {
            long registradas = aulaRepository.countByTipoEspacioAndBloqueIn(aula.getTipoEspacio(), bloquesDe(sede));
            if (registradas >= limite.getMaximo()) {
                throw new ReglaNegocioException("Ya hay " + registradas + " aulas de tipo " + aula.getTipoEspacio()
                        + " en " + sede + " y el inventario reporta un máximo de " + limite.getMaximo());
            }
        });
    }

    private List<Bloque> bloquesDe(Sede sede) {
        return Arrays.stream(Bloque.values())
                .filter(bloque -> sede == null || bloque.getSede() == sede)
                .toList();
    }

    // ESP-F-07: por ejemplo, los espacios multipropósito de un edificio específico.
    @Override
    @Transactional(readOnly = true)
    public List<Aula> listarPorBloqueYTipo(Bloque bloque, TipoEspacio tipoEspacio) {
        return aulaRepository.findByBloqueAndTipoEspacioAndActivaTrue(bloque, tipoEspacio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aula> listarActivas() {
        return aulaRepository.findByActivaTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aula> listarPorBloque(Bloque bloque) {
        return aulaRepository.findByBloqueAndActivaTrue(bloque);
    }

    @Override
    public Aula desactivar(Integer idAula) {
        Aula aula = buscarPorId(idAula);
        aula.setActiva(false);
        return aula;
    }

    // RF-22 / ESP-F-05: aulas activas, con capacidad y requisitos suficientes, sin auditorios.
    @Override
    @Transactional(readOnly = true)
    public List<Aula> sugerirAulas(Integer capacidadMinima, Set<CaracteristicaAula> requisitos, Sede sede) {
        List<Bloque> bloques = bloquesDe(sede);
        List<Aula> candidatas = aulaRepository.findByActivaTrueAndCapacidadGreaterThanEqualAndTipoEspacioNotAndBloqueIn(
                capacidadMinima, TipoEspacio.AUDITORIO, bloques);
        if (requisitos == null || requisitos.isEmpty()) {
            return candidatas;
        }
        return candidatas.stream()
                .filter(aula -> aula.getCaracteristicas().containsAll(requisitos))
                .toList();
    }
}
