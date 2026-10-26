package co.edu.unbosque.collegeAdministrator.dto.mapper;

import co.edu.unbosque.collegeAdministrator.dto.response.GrupoResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.ObservacionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.SesionResponse;
import co.edu.unbosque.collegeAdministrator.dto.response.VersionHorarioResponse;
import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.DetalleHorario;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.ObservacionAsignacion;
import co.edu.unbosque.collegeAdministrator.entity.VersionHorario;

import java.util.Comparator;

public final class HorarioMapper {

    private HorarioMapper() {
    }


    public static GrupoResponse aResponse(Grupo grupo, long cuposDisponibles) {
        return new GrupoResponse(grupo.getIdGrupo(), grupo.getCodigo(), AcademicoMapper.aResumen(grupo.getMateria()),
                grupo.getPeriodo().getIdPeriodo(), grupo.getPeriodo().getCodigo(),
                grupo.getDocente() == null ? null : grupo.getDocente().getIdUsuario(),
                grupo.getDocente() == null ? null : grupo.getDocente().getUsername(),
                grupo.getCupoMaximo(), cuposDisponibles, grupo.getEstado(),
                grupo.getSesiones().stream()
                        .sorted(Comparator.comparing((DetalleHorario s) -> s.getFranja().getDiaSemana())
                                .thenComparing(s -> s.getFranja().getHoraInicio()))
                        .map(HorarioMapper::aResponse)
                        .toList());
    }

    public static SesionResponse aResponse(DetalleHorario sesion) {
        Aula aula = sesion.getAula();
        return new SesionResponse(sesion.getIdDetalleHorario(), sesion.getFranja().getDiaSemana(),
                sesion.getFranja().getHoraInicio(), sesion.getFranja().getHoraFin(),
                aula == null ? null : aula.getIdAula(), aula == null ? null : aula.getCodigo(),
                aula == null ? null : aula.getBloque(), sesion.isUsoExcepcional());
    }

    public static ObservacionResponse aResponse(ObservacionAsignacion observacion) {
        Grupo grupo = observacion.getGrupo();
        return new ObservacionResponse(observacion.getIdObservacion(), grupo.getIdGrupo(),
                grupo.getMateria().getNombre() + " (grupo " + grupo.getCodigo() + ")",
                observacion.getDocente().getIdUsuario(), observacion.getDocente().getUsername(),
                observacion.getTexto(), observacion.getFecha(), observacion.getEstado());
    }

    public static VersionHorarioResponse aResponse(VersionHorario version) {
        return new VersionHorarioResponse(version.getIdVersion(), version.getNumero(), version.getFecha(),
                version.getUsuario(), version.getDescripcion());
    }
}
