package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.DetalleHorario;
import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.entity.VersionGrupo;
import co.edu.unbosque.collegeAdministrator.entity.VersionHorario;
import co.edu.unbosque.collegeAdministrator.entity.VersionSesion;
import co.edu.unbosque.collegeAdministrator.repository.GrupoRepository;
import co.edu.unbosque.collegeAdministrator.repository.PeriodoAcademicoRepository;
import co.edu.unbosque.collegeAdministrator.repository.VersionHorarioRepository;
import co.edu.unbosque.collegeAdministrator.security.UsuarioActual;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.VersionHorarioServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementación del versionado del horario.
 */
@Service
@Transactional
public class VersionHorarioServiceImpl implements VersionHorarioServiceAPI {

    /** El repositorio de versiones del horario. */
    private final VersionHorarioRepository versionHorarioRepository;
    /** El repositorio de periodos académicos. */
    private final PeriodoAcademicoRepository periodoAcademicoRepository;
    /** El repositorio de persistencia de grupos. */
    private final GrupoRepository grupoRepository;
    /** El componente que da acceso al usuario autenticado. */
    private final UsuarioActual usuarioActual;
    /** El servicio de auditoría donde se registran las acciones. */
    private final BitacoraServiceAPI bitacoraService;

    /**
     * Crea una instancia de {@code VersionHorarioServiceImpl} con las dependencias requeridas.
     *
     * @param versionHorarioRepository el repositorio de versiones del horario
     * @param periodoAcademicoRepository el repositorio de periodos académicos
     * @param grupoRepository el repositorio de persistencia de grupos
     * @param usuarioActual el componente que da acceso al usuario autenticado
     * @param bitacoraService el servicio de auditoría donde se registran las acciones
     */
    public VersionHorarioServiceImpl(VersionHorarioRepository versionHorarioRepository,
                                     PeriodoAcademicoRepository periodoAcademicoRepository,
                                     GrupoRepository grupoRepository,
                                     UsuarioActual usuarioActual,
                                     BitacoraServiceAPI bitacoraService) {
        this.versionHorarioRepository = versionHorarioRepository;
        this.periodoAcademicoRepository = periodoAcademicoRepository;
        this.grupoRepository = grupoRepository;
        this.usuarioActual = usuarioActual;
        this.bitacoraService = bitacoraService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VersionHorario guardarVersion(Integer idPeriodo, String descripcion) {
        PeriodoAcademico periodo = periodoAcademicoRepository.findById(idPeriodo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el periodo " + idPeriodo));
        int numero = versionHorarioRepository.findFirstByPeriodo_IdPeriodoOrderByNumeroDesc(idPeriodo)
                .map(ultima -> ultima.getNumero() + 1)
                .orElse(1);
        VersionHorario version = new VersionHorario(periodo, numero, usuarioActual.obtenerUsername(), descripcion);
        for (Grupo grupo : grupoRepository.findByPeriodo_IdPeriodo(idPeriodo)) {
            VersionGrupo versionGrupo = new VersionGrupo(grupo, grupo.getDocente());
            for (DetalleHorario sesion : grupo.getSesiones()) {
                versionGrupo.agregarSesion(new VersionSesion(copiar(sesion.getFranja()), sesion.getAula(),
                        sesion.isUsoExcepcional()));
            }
            version.agregarGrupo(versionGrupo);
        }
        VersionHorario guardada = versionHorarioRepository.save(version);
        eliminarVersionesAntiguas(idPeriodo);
        return guardada;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<VersionHorario> listarVersiones(Integer idPeriodo) {
        return versionHorarioRepository.findByPeriodo_IdPeriodoOrderByNumeroDesc(idPeriodo);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Los grupos creados después de la versión quedan sin docente ni sesiones, como estaban en ese
     * momento.
     */
    @Override
    public VersionHorario restaurar(Integer idVersion) {
        VersionHorario version = versionHorarioRepository.findById(idVersion)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la versión " + idVersion));
        PeriodoAcademico periodo = version.getPeriodo();
        if (periodo.estaCerrado() || periodo.horarioPublicado()) {
            throw new ReglaNegocioException("Solo se pueden restaurar versiones de un horario en borrador");
        }

        // Se copian los datos antes de guardar la versión actual, porque esa poda puede borrar la versión a restaurar.
        Map<Integer, EstadoGrupo> estadoPorGrupo = new HashMap<>();
        for (VersionGrupo versionGrupo : version.getGrupos()) {
            List<SesionGuardada> sesiones = new ArrayList<>();
            for (VersionSesion sesion : versionGrupo.getSesiones()) {
                sesiones.add(new SesionGuardada(copiar(sesion.getFranja()), sesion.getAula(), sesion.isUsoExcepcional()));
            }
            estadoPorGrupo.put(versionGrupo.getGrupo().getIdGrupo(), new EstadoGrupo(versionGrupo.getDocente(), sesiones));
        }
        int numeroRestaurado = version.getNumero();
        VersionHorario respaldo = guardarVersion(periodo.getIdPeriodo(), "Antes de restaurar la versión " + numeroRestaurado);

        // Los grupos creados después de la versión quedan sin docente ni sesiones, como estaban en ese momento.
        for (Grupo grupo : grupoRepository.findByPeriodo_IdPeriodo(periodo.getIdPeriodo())) {
            EstadoGrupo estado = estadoPorGrupo.getOrDefault(grupo.getIdGrupo(), new EstadoGrupo(null, List.of()));
            new ArrayList<>(grupo.getSesiones()).forEach(grupo::quitarSesion);
            grupo.setDocente(estado.docente());
            for (SesionGuardada sesion : estado.sesiones()) {
                grupo.agregarSesion(new DetalleHorario(sesion.franja(), sesion.aula(), sesion.usoExcepcional()));
            }
        }
        bitacoraService.registrar("RESTAURAR_VERSION", "Periodo " + periodo.getCodigo() + ", versión " + numeroRestaurado);
        return respaldo;
    }

    /**
     * Elimina las versiones que excedan el máximo permitido.
     *
     * @param idPeriodo el identificador del periodo académico
     */
    private void eliminarVersionesAntiguas(Integer idPeriodo) {
        List<VersionHorario> versiones = versionHorarioRepository.findByPeriodo_IdPeriodoOrderByNumeroDesc(idPeriodo);
        if (versiones.size() > MAX_VERSIONES) {
            versionHorarioRepository.deleteAll(versiones.subList(MAX_VERSIONES, versiones.size()));
        }
    }

    /**
     * Crea una copia independiente de una franja.
     *
     * @param franja la franja original
     * @return la copia de la franja
     */
    private FranjaHoraria copiar(FranjaHoraria franja) {
        return new FranjaHoraria(franja.getDiaSemana(), franja.getHoraInicio(), franja.getHoraFin());
    }

    /**
     * Copia en memoria de una sesión guardada.
     *
     * @param franja la franja horaria
     * @param aula el aula, o {@code null} si no tenía
     * @param usoExcepcional {@code true} si el aula era de uso excepcional
     */
    private record SesionGuardada(FranjaHoraria franja, Aula aula, boolean usoExcepcional) {
    }

    /**
     * Estado guardado de un grupo.
     *
     * @param docente el docente, o {@code null} si no tenía
     * @param sesiones las sesiones guardadas
     */
    private record EstadoGrupo(Docente docente, List<SesionGuardada> sesiones) {
    }
}
