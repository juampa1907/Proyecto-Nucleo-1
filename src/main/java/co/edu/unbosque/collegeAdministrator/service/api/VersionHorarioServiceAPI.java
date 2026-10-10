package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.VersionHorario;

import java.util.List;

/**
 * HU-29: historial de versiones del horario mientras está en borrador (se conservan las últimas 5).
 */
public interface VersionHorarioServiceAPI {

    int MAX_VERSIONES = 5;

    VersionHorario guardarVersion(Integer idPeriodo, String descripcion);

    List<VersionHorario> listarVersiones(Integer idPeriodo);

    VersionHorario restaurar(Integer idVersion);
}
