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

@Service
@Transactional
public class TipoVinculacionServiceImpl extends GenericServiceImpl<TipoVinculacion, Integer> implements TipoVinculacionServiceAPI {

    private final TipoVinculacionRepository tipoVinculacionRepository;

    public TipoVinculacionServiceImpl(TipoVinculacionRepository tipoVinculacionRepository) {
        this.tipoVinculacionRepository = tipoVinculacionRepository;
    }

    @Override
    protected JpaRepository<TipoVinculacion, Integer> getRepository() {
        return tipoVinculacionRepository;
    }

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

    @Override
    @Transactional(readOnly = true)
    public TipoVinculacion buscarPorNombre(String nombre) {
        return tipoVinculacionRepository.findByNombre(nombre)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el tipo de vinculación " + nombre));
    }
}
