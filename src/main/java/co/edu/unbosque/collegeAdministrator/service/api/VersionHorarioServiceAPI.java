package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.VersionHorario;

import java.util.List;


public interface VersionHorarioServiceAPI {

    int MAX_VERSIONES = 5;

    VersionHorario guardarVersion(Integer idPeriodo, String descripcion);

    List<VersionHorario> listarVersiones(Integer idPeriodo);

    VersionHorario restaurar(Integer idVersion);
}
