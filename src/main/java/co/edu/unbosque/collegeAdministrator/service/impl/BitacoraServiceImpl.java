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

@Service
@Transactional
public class BitacoraServiceImpl implements BitacoraServiceAPI {

    private static final int LONGITUD_MAXIMA_DETALLE = 1000;

    private final RegistroBitacoraRepository registroBitacoraRepository;
    private final UsuarioActual usuarioActual;

    public BitacoraServiceImpl(RegistroBitacoraRepository registroBitacoraRepository, UsuarioActual usuarioActual) {
        this.registroBitacoraRepository = registroBitacoraRepository;
        this.usuarioActual = usuarioActual;
    }

    @Override
    public void registrar(String accion, String detalle) {
        String detalleRecortado = detalle != null && detalle.length() > LONGITUD_MAXIMA_DETALLE
                ? detalle.substring(0, LONGITUD_MAXIMA_DETALLE)
                : detalle;
        registroBitacoraRepository.save(new RegistroBitacora(usuarioActual.obtenerUsername(), accion, detalleRecortado));
    }

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
