package co.edu.unbosque.collegeAdministrator.repository;

import co.edu.unbosque.collegeAdministrator.entity.RegistroBitacora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio de persistencia de {@code RegistroBitacora}.
 */
public interface RegistroBitacoraRepository extends JpaRepository<RegistroBitacora, Integer> {

    /**
     * Busca los registros de un rango de fechas, del más reciente al más antiguo.
     *
     * @param desde el inicio del rango de fechas
     * @param hasta el fin del rango de fechas
     * @return los registros del rango
     */
    List<RegistroBitacora> findByFechaBetweenOrderByFechaDesc(LocalDateTime desde, LocalDateTime hasta);

    /**
     * Busca los registros de un usuario en un rango de fechas, del más reciente al más antiguo.
     *
     * @param username el nombre de usuario
     * @param desde el inicio del rango de fechas
     * @param hasta el fin del rango de fechas
     * @return los registros que cumplen ambos criterios
     */
    List<RegistroBitacora> findByUsernameAndFechaBetweenOrderByFechaDesc(String username, LocalDateTime desde,
                                                                         LocalDateTime hasta);
}
