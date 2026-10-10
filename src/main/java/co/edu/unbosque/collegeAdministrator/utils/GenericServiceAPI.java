package co.edu.unbosque.collegeAdministrator.utils;

import java.util.List;

public interface GenericServiceAPI<T, ID> {

    T guardar(T entidad);

    T buscarPorId(ID id);

    List<T> listar();

    void eliminar(ID id);
}
