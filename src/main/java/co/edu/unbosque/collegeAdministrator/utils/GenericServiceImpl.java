package co.edu.unbosque.collegeAdministrator.utils;

import co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class GenericServiceImpl<T, ID> implements GenericServiceAPI<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    @Override
    @Transactional
    public T guardar(T entidad) {
        return getRepository().save(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public T buscarPorId(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un registro con id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> listar() {
        return getRepository().findAll();
    }

    @Override
    @Transactional
    public void eliminar(ID id) {
        if (!getRepository().existsById(id)) {
            throw new RecursoNoEncontradoException("No existe un registro con id " + id);
        }
        getRepository().deleteById(id);
    }
}
