package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.TipoVinculacion;
import co.edu.unbosque.collegeAdministrator.repository.TipoVinculacionRepository;
import co.edu.unbosque.collegeAdministrator.service.api.TipoVinculacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de las operaciones sobre los tipos de vinculación.
 */
@Service
@Transactional
public class TipoVinculacionServiceImpl extends GenericServiceImpl<TipoVinculacion, Integer> implements TipoVinculacionServiceAPI {

    /** El repositorio de tipos de vinculación docente. */
    private final TipoVinculacionRepository tipoVinculacionRepository;

    /**
     * Crea una instancia de {@code TipoVinculacionServiceImpl} con las dependencias requeridas.
     *
     * @param tipoVinculacionRepository el repositorio de tipos de vinculación docente
     */
    public TipoVinculacionServiceImpl(TipoVinculacionRepository tipoVinculacionRepository) {
        this.tipoVinculacionRepository = tipoVinculacionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<TipoVinculacion, Integer> getRepository() {
        return tipoVinculacionRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Exige horas máximas positivas y un nombre no repetido.
     */
    @Override
    public TipoVinculacion guardar(TipoVinculacion tipoVinculacion) {
        if (tipoVinculacion.getHorasMaximasSemanales() == null || tipoVinculacion.getHorasMaximasSemanales() <= 0) {
            throw new ReglaNegocioException("Las horas máximas semanales deben ser mayores a 0");
        }
        tipoVinculacionRepository.findByNombre(tipoVinculacion.getNombre())
                .filter(existente -> !existente.getIdTipoVinculacion().equals(tipoVinculacion.getIdTipoVinculacion()))
                .ifPresent(existente -> {
                    throw new ReglaNegocioException("Ya existe el tipo de vinculación " + tipoVinculacion.getNombre());
                });
        return super.guardar(tipoVinculacion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public TipoVinculacion buscarPorNombre(String nombre) {
        return tipoVinculacionRepository.findByNombre(nombre)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el tipo de vinculación " + nombre));
    }
}
