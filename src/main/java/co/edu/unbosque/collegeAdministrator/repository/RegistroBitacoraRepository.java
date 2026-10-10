package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.RegistroBitacora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistroBitacoraRepository extends JpaRepository<RegistroBitacora, Integer> {

    List<RegistroBitacora> findByFechaBetweenOrderByFechaDesc(LocalDateTime desde, LocalDateTime hasta);

    List<RegistroBitacora> findByUsernameAndFechaBetweenOrderByFechaDesc(String username, LocalDateTime desde,
                                                                         LocalDateTime hasta);
}
