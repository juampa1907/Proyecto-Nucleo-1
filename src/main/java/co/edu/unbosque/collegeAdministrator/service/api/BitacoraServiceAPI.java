package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.RegistroBitacora;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Operaciones de la bitácora de auditoría (HU-52).
 */
public interface BitacoraServiceAPI {

    /**
     * Registra una acción a nombre del usuario actual.
     *
     * @param accion el código de la acción
     * @param detalle el detalle de la acción
     */
    void registrar(String accion, String detalle);


    /**
     * Consulta la bitácora en un rango de fechas.
     *
     * @param desde el inicio del rango de fechas
     * @param hasta el fin del rango de fechas
     * @param username el usuario por el que se filtra, o {@code null} o vacío para todos
     * @return los registros, del más reciente al más antiguo
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el rango de
     *         fechas no es válido
     */
    List<RegistroBitacora> listar(LocalDateTime desde, LocalDateTime hasta, String username);
}
