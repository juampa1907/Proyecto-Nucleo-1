package co.edu.unbosque.collegeAdministrator.dto.mapper;

import co.edu.unbosque.collegeAdministrator.dto.response.InscripcionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.MatriculaResponse;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.MatriculaPeriodo;

/**
 * Conversiones de las inscripciones y matrículas a sus objetos de respuesta.
 */
public final class InscripcionMapper {

    /**
     * Constructor privado: esta clase no debe instanciarse.
     */
    private InscripcionMapper() {
    }

    /**
     * Convierte una inscripción en su respuesta.
     *
     * @param inscripcion la inscripción
     * @return la respuesta correspondiente
     */
    public static InscripcionResponse aResponse(InscripcionMateria inscripcion) {
        Grupo grupo = inscripcion.getGrupo();
        return new InscripcionResponse(inscripcion.getIdInscripcion(), inscripcion.getEstudiante().getIdUsuario(),
                inscripcion.getEstudiante().getUsername(), grupo.getIdGrupo(), grupo.getCodigo(),
                AcademicoMapper.aResumen(grupo.getMateria()), grupo.getPeriodo().getCodigo(),
                inscripcion.getEstado(), inscripcion.getNotaFinal());
    }

    /**
     * Convierte una matrícula en su respuesta.
     *
     * @param matricula la matrícula
     * @return la respuesta correspondiente
     */
    public static MatriculaResponse aResponse(MatriculaPeriodo matricula) {
        return new MatriculaResponse(matricula.getIdMatricula(), matricula.getEstudiante().getIdUsuario(),
                matricula.getPeriodo().getCodigo(), matricula.getFechaConfirmacion(), matricula.getCodigoVerificacion());
    }
}
