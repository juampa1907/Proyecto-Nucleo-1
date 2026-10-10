package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.RegistroBitacora;

import java.time.LocalDateTime;
import java.util.List;

/**
 * HU-52. No extiende GenericServiceAPI: los registros de auditoría no se editan ni se eliminan.
 */
public interface BitacoraServiceAPI {

    void registrar(String accion, String detalle);

    /**
     * @param username opcional: null para todos los usuarios.
     */
    List<RegistroBitacora> listar(LocalDateTime desde, LocalDateTime hasta, String username);
}
