package co.edu.unbosque.collegeAdministrator.dto.mapper;

import co.edu.unbosque.collegeAdministrator.dto.request.AulaRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.MateriaRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.ParametrosRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.PeriodoRequest;
import co.edu.unbosque.collegeAdministrator.dto.response.AulaResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.MateriaResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.MateriaResumen;
import co.edu.unbosque.collegeAdministrator.dto.response.ParametrosResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.PeriodoResponse;
import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;
import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Conversiones entre las entidades académicas y sus objetos de transferencia de datos.
 */
public final class AcademicoMapper {

    /**
     * Constructor privado: esta clase no debe instanciarse.
     */
    private AcademicoMapper() {
    }

    /**
     * Convierte una materia en su resumen.
     *
     * @param materia la materia
     * @return el resumen de la materia
     */
    public static MateriaResumen aResumen(Materia materia) {
        return new MateriaResumen(materia.getIdMateria(), materia.getCodigo(), materia.getNombre());
    }

    /**
     * Convierte una materia en su respuesta.
     *
     * @param materia la materia
     * @return la respuesta con los prerrequisitos y correquisitos ordenados por código
     */
    public static MateriaResponse aResponse(Materia materia) {
        return new MateriaResponse(materia.getIdMateria(), materia.getCodigo(), materia.getNombre(),
                materia.getNumeroCreditos(), materia.getHorasSemanales(), materia.getRequisitosAula(),
                resumenes(materia.getPrerrequisitos()), resumenes(materia.getCorrequisitos()));
    }

    /**
     * Crea una materia a partir de la solicitud.
     *
     * @param request los datos de la materia
     * @return la materia sin persistir
     */
    public static Materia nuevaMateria(MateriaRequest request) {
        return new Materia(request.codigo(), request.nombre(), request.numeroCreditos(), request.horasSemanales());
    }

    /**
     * Copia los datos de la solicitud en una materia existente.
     *
     * @param request los datos nuevos
     * @param materia la materia que se actualiza
     */
    public static void aplicar(MateriaRequest request, Materia materia) {
        materia.setCodigo(request.codigo());
        materia.setNombre(request.nombre());
        materia.setNumeroCreditos(request.numeroCreditos());
        materia.setHorasSemanales(request.horasSemanales());
    }

    /**
     * Convierte un aula en su respuesta.
     *
     * @param aula el aula
     * @return la respuesta con los datos del aula
     */
    public static AulaResponse aResponse(Aula aula) {
        return new AulaResponse(aula.getIdAula(), aula.getCodigo(), aula.getCapacidad(), aula.getTipoEspacio(),
                aula.getBloque(), aula.getBloque().getSede(), aula.getCaracteristicas(), aula.isActiva());
    }

    /**
     * Crea un aula a partir de la solicitud.
     *
     * @param request los datos del aula
     * @return el aula sin persistir
     */
    public static Aula nuevaAula(AulaRequest request) {
        Aula aula = new Aula(request.codigo(), request.capacidad(), request.tipoEspacio(), request.bloque());
        if (request.caracteristicas() != null) {
            request.caracteristicas().forEach(aula::agregarCaracteristica);
        }
        return aula;
    }

    // Reemplaza las características con las del request usando los métodos agregar/quitar de la entidad.
    /**
     * Copia los datos de la solicitud en un aula existente y reemplaza sus características.
     *
     * @param request los datos nuevos
     * @param aula el aula que se actualiza
     */
    public static void aplicar(AulaRequest request, Aula aula) {
        aula.setCodigo(request.codigo());
        aula.setCapacidad(request.capacidad());
        aula.setTipoEspacio(request.tipoEspacio());
        aula.setBloque(request.bloque());
        Set<CaracteristicaAula> nuevas = request.caracteristicas() == null ? Set.of() : request.caracteristicas();
        new HashSet<>(aula.getCaracteristicas()).stream()
                .filter(actual -> !nuevas.contains(actual))
                .forEach(aula::quitarCaracteristica);
        nuevas.forEach(aula::agregarCaracteristica);
    }

    /**
     * Convierte un periodo en su respuesta.
     *
     * @param periodo el periodo
     * @return la respuesta con los datos del periodo
     */
    public static PeriodoResponse aResponse(PeriodoAcademico periodo) {
        return new PeriodoResponse(periodo.getIdPeriodo(), periodo.getCodigo(), periodo.getFechaInicio(),
                periodo.getFechaFin(), periodo.getFechaAperturaInscripcion(), periodo.getFechaCierreInscripcion(),
                periodo.getEstado(), periodo.getEstadoHorario());
    }

    /**
     * Crea un periodo a partir de la solicitud.
     *
     * @param request los datos del periodo
     * @return el periodo sin persistir
     */
    public static PeriodoAcademico nuevoPeriodo(PeriodoRequest request) {
        return new PeriodoAcademico(request.codigo(), request.fechaInicio(), request.fechaFin(),
                request.fechaAperturaInscripcion(), request.fechaCierreInscripcion());
    }

    /**
     * Copia los datos de la solicitud en un periodo existente.
     *
     * @param request los datos nuevos
     * @param periodo el periodo que se actualiza
     */
    public static void aplicar(PeriodoRequest request, PeriodoAcademico periodo) {
        periodo.setCodigo(request.codigo());
        periodo.setFechaInicio(request.fechaInicio());
        periodo.setFechaFin(request.fechaFin());
        periodo.setFechaAperturaInscripcion(request.fechaAperturaInscripcion());
        periodo.setFechaCierreInscripcion(request.fechaCierreInscripcion());
    }

    /**
     * Convierte los parámetros académicos en su respuesta.
     *
     * @param p los parámetros académicos
     * @return la respuesta con los parámetros y el cupo máximo calculado
     */
    public static ParametrosResponse aResponse(ParametrosAcademicos p) {
        return new ParametrosResponse(p.getTamanoMaximoGrupo(), p.getToleranciaCupoPorcentaje(),
                p.calcularCupoMaximoPermitido(), p.getMinimoEstudiantesGrupo(), p.getHoraInicioSemana(),
                p.getHoraFinSemana(), p.getHoraInicioSabado(), p.getHoraFinSabado(), p.getInicioAlmuerzo(),
                p.getFinAlmuerzo(), p.getDuracionSesionMinutos(), p.getMaxSesionesSemanales(), p.getCreditosMinimos(),
                p.getCreditosMaximos(), p.getDiasAnticipacionAviso(), p.getUmbralSubutilizacionPorcentaje());
    }

    /**
     * Crea un objeto de parámetros a partir de la solicitud.
     *
     * @param r los datos de los parámetros
     * @return los parámetros sin persistir
     */
    public static ParametrosAcademicos aParametros(ParametrosRequest r) {
        ParametrosAcademicos p = new ParametrosAcademicos();
        p.setTamanoMaximoGrupo(r.tamanoMaximoGrupo());
        p.setToleranciaCupoPorcentaje(r.toleranciaCupoPorcentaje());
        p.setMinimoEstudiantesGrupo(r.minimoEstudiantesGrupo());
        p.setHoraInicioSemana(r.horaInicioSemana());
        p.setHoraFinSemana(r.horaFinSemana());
        p.setHoraInicioSabado(r.horaInicioSabado());
        p.setHoraFinSabado(r.horaFinSabado());
        p.setInicioAlmuerzo(r.inicioAlmuerzo());
        p.setFinAlmuerzo(r.finAlmuerzo());
        p.setDuracionSesionMinutos(r.duracionSesionMinutos());
        p.setMaxSesionesSemanales(r.maxSesionesSemanales());
        p.setCreditosMinimos(r.creditosMinimos());
        p.setCreditosMaximos(r.creditosMaximos());
        p.setDiasAnticipacionAviso(r.diasAnticipacionAviso());
        p.setUmbralSubutilizacionPorcentaje(r.umbralSubutilizacionPorcentaje());
        return p;
    }

    /**
     * Convierte materias en resúmenes ordenados por código.
     *
     * @param materias las materias
     * @return los resúmenes ordenados
     */
    private static List<MateriaResumen> resumenes(Collection<Materia> materias) {
        return materias.stream()
                .map(AcademicoMapper::aResumen)
                .sorted(Comparator.comparing(MateriaResumen::codigo))
                .toList();
    }
}
