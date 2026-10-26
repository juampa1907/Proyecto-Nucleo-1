package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.ResultadoReasignacion;
import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.MatriculaPeriodo;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.math.BigDecimal;
import java.util.List;

public interface InscripcionMateriaServiceAPI extends GenericServiceAPI<InscripcionMateria, Integer> {

    InscripcionMateria inscribir(Integer idEstudiante, Integer idGrupo);

    void cancelar(Integer idInscripcion);

    InscripcionMateria registrarNota(Integer idInscripcion, BigDecimal nota);

    List<InscripcionMateria> listarPorEstudianteYEstado(Integer idEstudiante, EstadoMateria estado);


    ResultadoReasignacion reasignarEstudiantes(Integer idGrupoOrigen, Integer idGrupoDestino);


    MatriculaPeriodo confirmarMatricula(Integer idEstudiante, Integer idPeriodo);


    List<InscripcionMateria> listarCargaDelPeriodo(Integer idEstudiante, Integer idPeriodo);
}
