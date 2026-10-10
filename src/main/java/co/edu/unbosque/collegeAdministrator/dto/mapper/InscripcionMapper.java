package co.edu.unbosque.collegeAdministrator.dto.mapper;

import co.edu.unbosque.collegeAdministrator.dto.response.InscripcionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.MatriculaResponse;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.MatriculaPeriodo;

public final class InscripcionMapper {

    private InscripcionMapper() {
    }

    public static InscripcionResponse aResponse(InscripcionMateria inscripcion) {
        Grupo grupo = inscripcion.getGrupo();
        return new InscripcionResponse(inscripcion.getIdInscripcion(), inscripcion.getEstudiante().getIdUsuario(),
                inscripcion.getEstudiante().getUsername(), grupo.getIdGrupo(), grupo.getCodigo(),
                AcademicoMapper.aResumen(grupo.getMateria()), grupo.getPeriodo().getCodigo(),
                inscripcion.getEstado(), inscripcion.getNotaFinal());
    }

    public static MatriculaResponse aResponse(MatriculaPeriodo matricula) {
        return new MatriculaResponse(matricula.getIdMatricula(), matricula.getEstudiante().getIdUsuario(),
                matricula.getPeriodo().getCodigo(), matricula.getFechaConfirmacion(), matricula.getCodigoVerificacion());
    }
}
