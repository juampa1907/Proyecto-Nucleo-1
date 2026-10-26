package co.edu.unbosque.collegeAdministrator.dto.mapper;

import co.edu.unbosque.collegeAdministrator.dto.response.BitacoraResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.LimiteInventarioResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.NotificacionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.PresupuestoResponse;
import co.edu.unbosque.collegeAdministrator.entity.LimiteInventarioAula;
import co.edu.unbosque.collegeAdministrator.entity.Notificacion;
import co.edu.unbosque.collegeAdministrator.entity.PresupuestoVinculacion;
import co.edu.unbosque.collegeAdministrator.entity.RegistroBitacora;


public final class AdministracionMapper {

    private AdministracionMapper() {
    }

    public static NotificacionResponse aResponse(Notificacion notificacion) {
        return new NotificacionResponse(notificacion.getIdNotificacion(), notificacion.getAsunto(),
                notificacion.getMensaje(), notificacion.getFecha(), notificacion.isLeida());
    }

    public static PresupuestoResponse aResponse(PresupuestoVinculacion presupuesto) {
        return new PresupuestoResponse(presupuesto.getIdPresupuesto(), presupuesto.getPeriodo().getIdPeriodo(),
                presupuesto.getTipoVinculacion().getIdTipoVinculacion(), presupuesto.getTipoVinculacion().getNombre(),
                presupuesto.getHorasPresupuestadas());
    }

    public static LimiteInventarioResponse aResponse(LimiteInventarioAula limite) {
        return new LimiteInventarioResponse(limite.getIdLimite(), limite.getTipoEspacio(), limite.getSede(), limite.getMaximo());
    }

    public static BitacoraResponse aResponse(RegistroBitacora registro) {
        return new BitacoraResponse(registro.getIdRegistro(), registro.getUsername(), registro.getFecha(),
                registro.getAccion(), registro.getDetalle());
    }
}
