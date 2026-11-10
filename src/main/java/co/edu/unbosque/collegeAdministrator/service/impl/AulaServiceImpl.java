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

/**
 * Implementación de las operaciones de negocio sobre las aulas.
 */
@Service
@Transactional
public class AulaServiceImpl extends GenericServiceImpl<Aula, Integer> implements AulaServiceAPI {

    /** El repositorio de persistencia de aulas. */
    private final AulaRepository aulaRepository;
    /** El repositorio de los topes de inventario de aulas. */
    private final LimiteInventarioAulaRepository limiteInventarioAulaRepository;

    /**
     * Crea una instancia de {@code AulaServiceImpl} con las dependencias requeridas.
     *
     * @param aulaRepository el repositorio de persistencia de aulas
     * @param limiteInventarioAulaRepository el repositorio de los topes de inventario de aulas
     */
    public AulaServiceImpl(AulaRepository aulaRepository, LimiteInventarioAulaRepository limiteInventarioAulaRepository) {
        this.aulaRepository = aulaRepository;
        this.limiteInventarioAulaRepository = limiteInventarioAulaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<Aula, Integer> getRepository() {
        return aulaRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Exige capacidad positiva y bloque, restringe las aulas de cómputo a Usaquén y, al crear, valida el
     * tope de inventario.
     */
    @Override
    public Aula guardar(Aula aula) {
        if (aula.getCapacidad() == null || aula.getCapacidad() <= 0) {
            throw new ReglaNegocioException("La capacidad del aula debe ser mayor a 0");
        }
        if (aula.getBloque() == null) {
            throw new ReglaNegocioException("El bloque del aula es obligatorio");
        }

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

    /**
     * Verifica que un aula nueva no supere el tope de su tipo de espacio y sede.
     *
     * @param aula el aula evaluada
     * @throws ReglaNegocioException si se alcanzó el tope
     */
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

    /**
     * Obtiene los bloques de una sede.
     *
     * @param sede la sede, o {@code null} para todas
     * @return los bloques correspondientes
     */
    private List<Bloque> bloquesDe(Sede sede) {
        return Arrays.stream(Bloque.values())
                .filter(bloque -> sede == null || bloque.getSede() == sede)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Aula> listarPorBloqueYTipo(Bloque bloque, TipoEspacio tipoEspacio) {
        return aulaRepository.findByBloqueAndTipoEspacioAndActivaTrue(bloque, tipoEspacio);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Aula> listarActivas() {
        return aulaRepository.findByActivaTrue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Aula> listarPorBloque(Bloque bloque) {
        return aulaRepository.findByBloqueAndActivaTrue(bloque);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Aula desactivar(Integer idAula) {
        Aula aula = buscarPorId(idAula);
        aula.setActiva(false);
        return aula;
    }

    /**
     * {@inheritDoc}
     */
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
