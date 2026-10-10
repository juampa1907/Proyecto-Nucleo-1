package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByUsuario_IdUsuarioOrderByFechaDesc(Integer idUsuario);
}
