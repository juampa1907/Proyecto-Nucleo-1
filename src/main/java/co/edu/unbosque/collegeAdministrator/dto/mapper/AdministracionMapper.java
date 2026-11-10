package co.edu.unbosque.collegeAdministrator.dto.mapper;

import co.edu.unbosque.collegeAdministrator.dto.response.BitacoraResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.LimiteInventarioResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.NotificacionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.PresupuestoResponse;
import co.edu.unbosque.collegeAdministrator.entity.LimiteInventarioAula;
import co.edu.unbosque.collegeAdministrator.entity.Notificacion;
import co.edu.unbosque.collegeAdministrator.entity.PresupuestoVinculacion;
import co.edu.unbosque.collegeAdministrator.entity.RegistroBitacora;


/**
 * Conversiones de las entidades administrativas a sus objetos de respuesta.
 */
public final class AdministracionMapper {

    /**
     * Constructor privado: esta clase no debe instanciarse.
     */
    private AdministracionMapper() {
    }

    /**
     * Convierte una notificación en su respuesta.
     *
     * @param notificacion la notificación
     * @return la respuesta correspondiente
     */
    public static NotificacionResponse aResponse(Notificacion notificacion) {
        return new NotificacionResponse(notificacion.getIdNotificacion(), notificacion.getAsunto(),
                notificacion.getMensaje(), notificacion.getFecha(), notificacion.isLeida());
    }

    /**
     * Convierte un presupuesto en su respuesta.
     *
     * @param presupuesto el presupuesto
     * @return la respuesta correspondiente
     */
    public static PresupuestoResponse aResponse(PresupuestoVinculacion presupuesto) {
        return new PresupuestoResponse(presupuesto.getIdPresupuesto(), presupuesto.getPeriodo().getIdPeriodo(),
                presupuesto.getTipoVinculacion().getIdTipoVinculacion(), presupuesto.getTipoVinculacion().getNombre(),
                presupuesto.getHorasPresupuestadas());
    }

    /**
     * Convierte un tope de inventario en su respuesta.
     *
     * @param limite el tope de inventario
     * @return la respuesta correspondiente
     */
    public static LimiteInventarioResponse aResponse(LimiteInventarioAula limite) {
        return new LimiteInventarioResponse(limite.getIdLimite(), limite.getTipoEspacio(), limite.getSede(), limite.getMaximo());
    }

    /**
     * Convierte un registro de bitácora en su respuesta.
     *
     * @param registro el registro
     * @return la respuesta correspondiente
     */
    public static BitacoraResponse aResponse(RegistroBitacora registro) {
        return new BitacoraResponse(registro.getIdRegistro(), registro.getUsername(), registro.getFecha(),
                registro.getAccion(), registro.getDetalle());
    }
}
