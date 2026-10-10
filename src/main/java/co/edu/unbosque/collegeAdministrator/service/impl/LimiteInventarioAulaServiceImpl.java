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

@Service
@Transactional
public class LimiteInventarioAulaServiceImpl extends GenericServiceImpl<LimiteInventarioAula, Integer>
        implements LimiteInventarioAulaServiceAPI {

    private final LimiteInventarioAulaRepository limiteInventarioAulaRepository;
    private final BitacoraServiceAPI bitacoraService;

    public LimiteInventarioAulaServiceImpl(LimiteInventarioAulaRepository limiteInventarioAulaRepository,
                                           BitacoraServiceAPI bitacoraService) {
        this.limiteInventarioAulaRepository = limiteInventarioAulaRepository;
        this.bitacoraService = bitacoraService;
    }

    @Override
    protected JpaRepository<LimiteInventarioAula, Integer> getRepository() {
        return limiteInventarioAulaRepository;
    }

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
