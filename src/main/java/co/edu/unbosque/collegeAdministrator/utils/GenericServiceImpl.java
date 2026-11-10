package co.edu.unbosque.collegeAdministrator.utils;

import co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación base de las operaciones CRUD sobre un repositorio JPA.
 *
 * @param <T> el tipo de la entidad
 * @param <ID> el tipo del identificador de la entidad
 */
public abstract class GenericServiceImpl<T, ID> implements GenericServiceAPI<T, ID> {

    /**
     * Obtiene el repositorio sobre el que operan los métodos CRUD.
     *
     * @return el repositorio de la entidad
     */
    protected abstract JpaRepository<T, ID> getRepository();

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public T guardar(T entidad) {
        return getRepository().save(entidad);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public T buscarPorId(ID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un registro con id " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> listar() {
        return getRepository().findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void eliminar(ID id) {
        if (!getRepository().existsById(id)) {
            throw new RecursoNoEncontradoException("No existe un registro con id " + id);
        }
        getRepository().deleteById(id);
    }
}
