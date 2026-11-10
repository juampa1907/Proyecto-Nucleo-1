package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.LimiteInventarioAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import co.edu.unbosque.collegeAdministrator.repository.LimiteInventarioAulaRepository;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.LimiteInventarioAulaServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de las operaciones sobre los topes de inventario de aulas.
 */
@Service
@Transactional
public class LimiteInventarioAulaServiceImpl extends GenericServiceImpl<LimiteInventarioAula, Integer>
        implements LimiteInventarioAulaServiceAPI {

    /** El repositorio de los topes de inventario de aulas. */
    private final LimiteInventarioAulaRepository limiteInventarioAulaRepository;
    /** El servicio de auditoría donde se registran las acciones. */
    private final BitacoraServiceAPI bitacoraService;

    /**
     * Crea una instancia de {@code LimiteInventarioAulaServiceImpl} con las dependencias requeridas.
     *
     * @param limiteInventarioAulaRepository el repositorio de los topes de inventario de aulas
     * @param bitacoraService el servicio de auditoría donde se registran las acciones
     */
    public LimiteInventarioAulaServiceImpl(LimiteInventarioAulaRepository limiteInventarioAulaRepository,
                                           BitacoraServiceAPI bitacoraService) {
        this.limiteInventarioAulaRepository = limiteInventarioAulaRepository;
        this.bitacoraService = bitacoraService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<LimiteInventarioAula, Integer> getRepository() {
        return limiteInventarioAulaRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LimiteInventarioAula definirLimite(TipoEspacio tipoEspacio, Sede sede, Integer maximo) {
        if (tipoEspacio == null || sede == null) {
            throw new ReglaNegocioException("El tipo de espacio y la sede son obligatorios");
        }
        if (maximo == null || maximo < 0) {
            throw new ReglaNegocioException("El tope de aulas no puede ser negativo");
        }
        LimiteInventarioAula limite = limiteInventarioAulaRepository.findByTipoEspacioAndSede(tipoEspacio, sede)
                .orElseGet(() -> new LimiteInventarioAula(tipoEspacio, sede, maximo));
        limite.setMaximo(maximo);
        bitacoraService.registrar("DEFINIR_TOPE_AULAS", tipoEspacio + " en " + sede + ": " + maximo);
        return limiteInventarioAulaRepository.save(limite);
    }
}
