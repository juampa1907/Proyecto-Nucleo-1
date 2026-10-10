package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

public interface DocenteServiceAPI extends GenericServiceAPI<Docente, Integer> {

    Docente buscarPorUsername(String username);

    Docente asignarTipoVinculacion(Integer idDocente, Integer idTipoVinculacion);

    Docente habilitarMateria(Integer idDocente, Integer idMateria);

    Docente deshabilitarMateria(Integer idDocente, Integer idMateria);

    Docente agregarRestriccion(Integer idDocente, FranjaHoraria franja);

    Docente quitarRestriccion(Integer idDocente, Integer idRestriccion);

    double calcularHorasAsignadas(Integer idDocente, Integer idPeriodo);

    double calcularHorasDisponibles(Integer idDocente, Integer idPeriodo);

    List<Docente> listarPorTipoVinculacion(Integer idTipoVinculacion);

    List<Docente> listarHabilitadosParaMateria(Integer idMateria);
}
