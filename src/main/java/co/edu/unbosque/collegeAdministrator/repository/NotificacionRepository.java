package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de persistencia de {@code Notificacion}.
 */
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    /**
     * Busca las notificaciones de un usuario, de la más reciente a la más antigua.
     *
     * @param idUsuario el identificador del usuario
     * @return las notificaciones ordenadas por fecha descendente
     */
    List<Notificacion> findByUsuario_IdUsuarioOrderByFechaDesc(Integer idUsuario);
}
