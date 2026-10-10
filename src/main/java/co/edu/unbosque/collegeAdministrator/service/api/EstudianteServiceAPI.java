package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Estudiante;
import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

public interface EstudianteServiceAPI extends GenericServiceAPI<Estudiante, Integer> {

    Estudiante buscarPorUsername(String username);

    List<Materia> listarMateriasActuales(Integer idEstudiante);

    int calcularCreditosAprobados(Integer idEstudiante);
}
