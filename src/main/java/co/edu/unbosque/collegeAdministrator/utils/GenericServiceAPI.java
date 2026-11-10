package co.edu.unbosque.collegeAdministrator.utils;

import java.util.List;

/**
 * Contrato genérico de las operaciones CRUD de los servicios.
 *
 * @param <T> el tipo de la entidad
 * @param <ID> el tipo del identificador de la entidad
 */
public interface GenericServiceAPI<T, ID> {

    /**
     * Crea o actualiza una entidad.
     *
     * @param entidad la entidad que se guarda
     * @return la entidad guardada
     */
    T guardar(T entidad);

    /**
     * Busca una entidad por su identificador.
     *
     * @param id el identificador
     * @return la entidad encontrada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si no existe
     */
    T buscarPorId(ID id);

    /**
     * Lista todas las entidades.
     *
     * @return las entidades registradas
     */
    List<T> listar();

    /**
     * Elimina una entidad por su identificador.
     *
     * @param id el identificador
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si no existe
     */
    void eliminar(ID id);
}
