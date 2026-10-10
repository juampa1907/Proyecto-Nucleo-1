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
 * Materias, aulas, periodos y parámetros académicos.
 */
public final class AcademicoMapper {

    private AcademicoMapper() {
    }

    public static MateriaResumen aResumen(Materia materia) {
        return new MateriaResumen(materia.getIdMateria(), materia.getCodigo(), materia.getNombre());
    }

    public static MateriaResponse aResponse(Materia materia) {
        return new MateriaResponse(materia.getIdMateria(), materia.getCodigo(), materia.getNombre(),
                materia.getNumeroCreditos(), materia.getHorasSemanales(), materia.getRequisitosAula(),
                resumenes(materia.getPrerrequisitos()), resumenes(materia.getCorrequisitos()));
    }

    public static Materia nuevaMateria(MateriaRequest request) {
        return new Materia(request.codigo(), request.nombre(), request.numeroCreditos(), request.horasSemanales());
    }

    public static void aplicar(MateriaRequest request, Materia materia) {
        materia.setCodigo(request.codigo());
        materia.setNombre(request.nombre());
        materia.setNumeroCreditos(request.numeroCreditos());
        materia.setHorasSemanales(request.horasSemanales());
    }

    public static AulaResponse aResponse(Aula aula) {
        return new AulaResponse(aula.getIdAula(), aula.getCodigo(), aula.getCapacidad(), aula.getTipoEspacio(),
                aula.getBloque(), aula.getBloque().getSede(), aula.getCaracteristicas(), aula.isActiva());
    }

    public static Aula nuevaAula(AulaRequest request) {
        Aula aula = new Aula(request.codigo(), request.capacidad(), request.tipoEspacio(), request.bloque());
        if (request.caracteristicas() != null) {
            request.caracteristicas().forEach(aula::agregarCaracteristica);
        }
        return aula;
    }

    // Reemplaza las características con las del request usando los métodos agregar/quitar de la entidad.
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

    public static PeriodoResponse aResponse(PeriodoAcademico periodo) {
        return new PeriodoResponse(periodo.getIdPeriodo(), periodo.getCodigo(), periodo.getFechaInicio(),
                periodo.getFechaFin(), periodo.getFechaAperturaInscripcion(), periodo.getFechaCierreInscripcion(),
                periodo.getEstado(), periodo.getEstadoHorario());
    }

    public static PeriodoAcademico nuevoPeriodo(PeriodoRequest request) {
        return new PeriodoAcademico(request.codigo(), request.fechaInicio(), request.fechaFin(),
                request.fechaAperturaInscripcion(), request.fechaCierreInscripcion());
    }

    public static void aplicar(PeriodoRequest request, PeriodoAcademico periodo) {
        periodo.setCodigo(request.codigo());
        periodo.setFechaInicio(request.fechaInicio());
        periodo.setFechaFin(request.fechaFin());
        periodo.setFechaAperturaInscripcion(request.fechaAperturaInscripcion());
        periodo.setFechaCierreInscripcion(request.fechaCierreInscripcion());
    }

    public static ParametrosResponse aResponse(ParametrosAcademicos p) {
        return new ParametrosResponse(p.getTamanoMaximoGrupo(), p.getToleranciaCupoPorcentaje(),
                p.calcularCupoMaximoPermitido(), p.getMinimoEstudiantesGrupo(), p.getHoraInicioSemana(),
                p.getHoraFinSemana(), p.getHoraInicioSabado(), p.getHoraFinSabado(), p.getInicioAlmuerzo(),
                p.getFinAlmuerzo(), p.getDuracionSesionMinutos(), p.getMaxSesionesSemanales(), p.getCreditosMinimos(),
                p.getCreditosMaximos(), p.getDiasAnticipacionAviso(), p.getUmbralSubutilizacionPorcentaje());
    }

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

    private static List<MateriaResumen> resumenes(Collection<Materia> materias) {
        return materias.stream()
                .map(AcademicoMapper::aResumen)
                .sorted(Comparator.comparing(MateriaResumen::codigo))
                .toList();
    }
}
