package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.RegistroBitacora;
import co.edu.unbosque.collegeAdministrator.repository.RegistroBitacoraRepository;
import co.edu.unbosque.collegeAdministrator.security.UsuarioActual;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación de la bitácora de auditoría.
 */
@Service
@Transactional
public class BitacoraServiceImpl implements BitacoraServiceAPI {

    /** Longitud máxima del detalle de un registro. */
    private static final int LONGITUD_MAXIMA_DETALLE = 1000;

    /** El repositorio de los registros de bitácora. */
    private final RegistroBitacoraRepository registroBitacoraRepository;
    /** El componente que da acceso al usuario autenticado. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea una instancia de {@code BitacoraServiceImpl} con las dependencias requeridas.
     *
     * @param registroBitacoraRepository el repositorio de los registros de bitácora
     * @param usuarioActual el componente que da acceso al usuario autenticado
     */
    public BitacoraServiceImpl(RegistroBitacoraRepository registroBitacoraRepository, UsuarioActual usuarioActual) {
        this.registroBitacoraRepository = registroBitacoraRepository;
        this.usuarioActual = usuarioActual;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Recorta el detalle que supere la longitud máxima.
     */
    @Override
    public void registrar(String accion, String detalle) {
        String detalleRecortado = detalle != null && detalle.length() > LONGITUD_MAXIMA_DETALLE
                ? detalle.substring(0, LONGITUD_MAXIMA_DETALLE)
                : detalle;
        registroBitacoraRepository.save(new RegistroBitacora(usuarioActual.obtenerUsername(), accion, detalleRecortado));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<RegistroBitacora> listar(LocalDateTime desde, LocalDateTime hasta, String username) {
        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new ReglaNegocioException("El rango de fechas de la bitácora no es válido");
        }
        if (username == null || username.isBlank()) {
            return registroBitacoraRepository.findByFechaBetweenOrderByFechaDesc(desde, hasta);
        }
        return registroBitacoraRepository.findByUsernameAndFechaBetweenOrderByFechaDesc(username, desde, hasta);
    }
}
