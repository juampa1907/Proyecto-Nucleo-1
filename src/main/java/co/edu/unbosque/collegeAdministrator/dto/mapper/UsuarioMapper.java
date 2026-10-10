package co.edu.unbosque.collegeAdministrator.dto.mapper;

import co.edu.unbosque.collegeAdministrator.dto.request.DocenteRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.EstudianteRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.TipoVinculacionRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.DocenteResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.EstudianteResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.RestriccionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.TipoVinculacionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.UsuarioResponse;
import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.Estudiante;
import co.edu.unbosque.collegeAdministrator.entity.RestriccionHorariaDocente;
import co.edu.unbosque.collegeAdministrator.entity.TipoVinculacion;
import co.edu.unbosque.collegeAdministrator.entity.Usuario;

import java.util.Comparator;

/**
 * Usuarios, estudiantes, docentes y tipos de vinculación. Ninguna respuesta incluye la contraseña.
 */
public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioResponse aResponse(Usuario usuario) {
        String tipo = usuario instanceof Estudiante ? "ESTUDIANTE" : usuario instanceof Docente ? "DOCENTE" : "USUARIO";
        return new UsuarioResponse(usuario.getIdUsuario(), usuario.getUsername(), usuario.getCorreo(), tipo,
                usuario.getRoles(), usuario.getFechaInitSesion(), usuario.getIpUltimoInicioSesion(), usuario.getBloqueadoHasta());
    }

    public static EstudianteResponse aResponse(Estudiante estudiante) {
        return new EstudianteResponse(estudiante.getIdUsuario(), estudiante.getUsername(), estudiante.getCorreo(),
                estudiante.getNumeroSemestre(), estudiante.getCreditosAprobados());
    }

    public static DocenteResponse aResponse(Docente docente) {
        return new DocenteResponse(docente.getIdUsuario(), docente.getUsername(), docente.getCorreo(),
                docente.getEscalafon(), docente.getEspecialidad(),
                docente.getTipoVinculacion() == null ? null : aResponse(docente.getTipoVinculacion()),
                docente.getMateriasHabilitadas().stream()
                        .map(AcademicoMapper::aResumen)
                        .sorted(Comparator.comparing(m -> m.codigo()))
                        .toList(),
                docente.getRestriccionesHorarias().stream()
                        .map(UsuarioMapper::aResponse)
                        .toList());
    }

    public static RestriccionResponse aResponse(RestriccionHorariaDocente restriccion) {
        return new RestriccionResponse(restriccion.getIdRestriccion(), restriccion.getFranja().getDiaSemana(),
                restriccion.getFranja().getHoraInicio(), restriccion.getFranja().getHoraFin());
    }

    public static TipoVinculacionResponse aResponse(TipoVinculacion tipo) {
        return new TipoVinculacionResponse(tipo.getIdTipoVinculacion(), tipo.getNombre(), tipo.getHorasMaximasSemanales());
    }

    public static Estudiante nuevoEstudiante(EstudianteRequest request) {
        return new Estudiante(request.username(), request.correo(), request.password(), request.numeroSemestre());
    }

    public static Docente nuevoDocente(DocenteRequest request) {
        Docente docente = new Docente(request.username(), request.correo(), request.password());
        docente.setEscalafon(request.escalafon());
        docente.setEspecialidad(request.especialidad());
        return docente;
    }

    public static TipoVinculacion nuevoTipoVinculacion(TipoVinculacionRequest request) {
        return new TipoVinculacion(request.nombre(), request.horasMaximasSemanales());
    }

    public static void aplicar(TipoVinculacionRequest request, TipoVinculacion tipo) {
        tipo.setNombre(request.nombre());
        tipo.setHorasMaximasSemanales(request.horasMaximasSemanales());
    }
}
