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
 * Conversiones entre los usuarios y sus objetos de transferencia de datos.
 */
public final class UsuarioMapper {

    /**
     * Constructor privado: esta clase no debe instanciarse.
     */
    private UsuarioMapper() {
    }

    /**
     * Convierte un usuario en su respuesta e identifica su tipo.
     *
     * @param usuario el usuario
     * @return la respuesta correspondiente
     */
    public static UsuarioResponse aResponse(Usuario usuario) {
        String tipo = usuario instanceof Estudiante ? "ESTUDIANTE" : usuario instanceof Docente ? "DOCENTE" : "USUARIO";
        return new UsuarioResponse(usuario.getIdUsuario(), usuario.getUsername(), usuario.getCorreo(), tipo,
                usuario.getRoles(), usuario.getFechaInitSesion(), usuario.getIpUltimoInicioSesion(), usuario.getBloqueadoHasta());
    }

    /**
     * Convierte un estudiante en su respuesta.
     *
     * @param estudiante el estudiante
     * @return la respuesta correspondiente
     */
    public static EstudianteResponse aResponse(Estudiante estudiante) {
        return new EstudianteResponse(estudiante.getIdUsuario(), estudiante.getUsername(), estudiante.getCorreo(),
                estudiante.getNumeroSemestre(), estudiante.getCreditosAprobados());
    }

    /**
     * Convierte un docente en su respuesta, con las materias habilitadas ordenadas por código.
     *
     * @param docente el docente
     * @return la respuesta correspondiente
     */
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

    /**
     * Convierte una restricción horaria en su respuesta.
     *
     * @param restriccion la restricción
     * @return la respuesta correspondiente
     */
    public static RestriccionResponse aResponse(RestriccionHorariaDocente restriccion) {
        return new RestriccionResponse(restriccion.getIdRestriccion(), restriccion.getFranja().getDiaSemana(),
                restriccion.getFranja().getHoraInicio(), restriccion.getFranja().getHoraFin());
    }

    /**
     * Convierte un tipo de vinculación en su respuesta.
     *
     * @param tipo el tipo de vinculación
     * @return la respuesta correspondiente
     */
    public static TipoVinculacionResponse aResponse(TipoVinculacion tipo) {
        return new TipoVinculacionResponse(tipo.getIdTipoVinculacion(), tipo.getNombre(), tipo.getHorasMaximasSemanales());
    }

    /**
     * Crea un estudiante a partir de la solicitud.
     *
     * @param request los datos del estudiante
     * @return el estudiante sin persistir
     */
    public static Estudiante nuevoEstudiante(EstudianteRequest request) {
        return new Estudiante(request.username(), request.correo(), request.password(), request.numeroSemestre());
    }

    /**
     * Crea un docente a partir de la solicitud, sin asignar su tipo de vinculación.
     *
     * @param request los datos del docente
     * @return el docente sin persistir
     */
    public static Docente nuevoDocente(DocenteRequest request) {
        Docente docente = new Docente(request.username(), request.correo(), request.password());
        docente.setEscalafon(request.escalafon());
        docente.setEspecialidad(request.especialidad());
        return docente;
    }

    /**
     * Crea un tipo de vinculación a partir de la solicitud.
     *
     * @param request los datos del tipo de vinculación
     * @return el tipo de vinculación sin persistir
     */
    public static TipoVinculacion nuevoTipoVinculacion(TipoVinculacionRequest request) {
        return new TipoVinculacion(request.nombre(), request.horasMaximasSemanales());
    }

    /**
     * Copia los datos de la solicitud en un tipo de vinculación existente.
     *
     * @param request los datos nuevos
     * @param tipo el tipo de vinculación que se actualiza
     */
    public static void aplicar(TipoVinculacionRequest request, TipoVinculacion tipo) {
        tipo.setNombre(request.nombre());
        tipo.setHorasMaximasSemanales(request.horasMaximasSemanales());
    }
}
