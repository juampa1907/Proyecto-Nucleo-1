package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.ObservacionAsignacion;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

public interface ObservacionAsignacionServiceAPI extends GenericServiceAPI<ObservacionAsignacion, Integer> {

    ObservacionAsignacion registrar(Integer idGrupo, Integer idDocente, String texto);

    ObservacionAsignacion resolver(Integer idObservacion);

    List<ObservacionAsignacion> listarPendientes(Integer idPeriodo);
}
