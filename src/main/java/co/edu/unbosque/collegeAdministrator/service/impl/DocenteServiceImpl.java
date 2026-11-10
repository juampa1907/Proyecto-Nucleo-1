package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.Docente;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.RestriccionHorariaDocente;
import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoGrupo;
import co.edu.unbosque.collegeAdministrator.repository.DocenteRepository;
import co.edu.unbosque.collegeAdministrator.repository.GrupoRepository;
import co.edu.unbosque.collegeAdministrator.service.api.DocenteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.MateriaServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.TipoVinculacionServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.UsuarioServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de las operaciones de negocio sobre los docentes.
 */
@Service
@Transactional
public class DocenteServiceImpl extends GenericServiceImpl<Docente, Integer> implements DocenteServiceAPI {

    /** El repositorio de persistencia de docentes. */
    private final DocenteRepository docenteRepository;
    /** El repositorio de persistencia de grupos. */
    private final GrupoRepository grupoRepository;
    /** El servicio de gestión de usuarios. */
    private final UsuarioServiceAPI usuarioService;
    /** El servicio de tipos de vinculación docente. */
    private final TipoVinculacionServiceAPI tipoVinculacionService;
    /** El servicio de gestión de materias. */
    private final MateriaServiceAPI materiaService;

    /**
     * Crea una instancia de {@code DocenteServiceImpl} con las dependencias requeridas.
     *
     * @param docenteRepository el repositorio de persistencia de docentes
     * @param grupoRepository el repositorio de persistencia de grupos
     * @param usuarioService el servicio de gestión de usuarios
     * @param tipoVinculacionService el servicio de tipos de vinculación docente
     * @param materiaService el servicio de gestión de materias
     */
    public DocenteServiceImpl(DocenteRepository docenteRepository,
                              GrupoRepository grupoRepository,
                              UsuarioServiceAPI usuarioService,
                              TipoVinculacionServiceAPI tipoVinculacionService,
                              MateriaServiceAPI materiaService) {
        this.docenteRepository = docenteRepository;
        this.grupoRepository = grupoRepository;
        this.usuarioService = usuarioService;
        this.tipoVinculacionService = tipoVinculacionService;
        this.materiaService = materiaService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<Docente, Integer> getRepository() {
        return docenteRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Al crear un docente valida sus datos y cifra su contraseña.
     */
    @Override
    public Docente guardar(Docente docente) {
        if (docente.getIdUsuario() == null) {
            usuarioService.prepararNuevoUsuario(docente);
        }
        return super.guardar(docente);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Docente buscarPorUsername(String username) {
        return docenteRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el docente " + username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Docente asignarTipoVinculacion(Integer idDocente, Integer idTipoVinculacion) {
        Docente docente = buscarPorId(idDocente);
        docente.setTipoVinculacion(tipoVinculacionService.buscarPorId(idTipoVinculacion));
        return docente;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Docente habilitarMateria(Integer idDocente, Integer idMateria) {
        Docente docente = buscarPorId(idDocente);
        docente.habilitarMateria(materiaService.buscarPorId(idMateria));
        return docente;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Docente deshabilitarMateria(Integer idDocente, Integer idMateria) {
        Docente docente = buscarPorId(idDocente);
        docente.deshabilitarMateria(materiaService.buscarPorId(idMateria));
        return docente;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Docente agregarRestriccion(Integer idDocente, FranjaHoraria franja) {
        Docente docente = buscarPorId(idDocente);
        for (RestriccionHorariaDocente restriccion : docente.getRestriccionesHorarias()) {
            if (restriccion.getFranja().seCruzaCon(franja)) {
                throw new ReglaNegocioException("La franja " + franja
                        + " se solapa con la restricción existente " + restriccion.getFranja());
            }
        }
        docente.agregarRestriccion(new RestriccionHorariaDocente(franja));
        return docente;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Docente quitarRestriccion(Integer idDocente, Integer idRestriccion) {
        Docente docente = buscarPorId(idDocente);
        RestriccionHorariaDocente restriccion = docente.getRestriccionesHorarias().stream()
                .filter(r -> r.getIdRestriccion().equals(idRestriccion))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El docente no tiene una restricción con id " + idRestriccion));
        docente.quitarRestriccion(restriccion);
        return docente;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public double calcularHorasAsignadas(Integer idDocente, Integer idPeriodo) {
        buscarPorId(idDocente);
        return grupoRepository.findByDocente_IdUsuarioAndPeriodo_IdPeriodo(idDocente, idPeriodo).stream()
                .filter(grupo -> grupo.getEstado() != EstadoGrupo.CERRADO)
                .mapToDouble(Grupo::calcularHorasSemanales)
                .sum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public double calcularHorasDisponibles(Integer idDocente, Integer idPeriodo) {
        Docente docente = buscarPorId(idDocente);
        if (docente.getTipoVinculacion() == null) {
            throw new ReglaNegocioException("El docente " + docente.getUsername() + " no tiene tipo de vinculación asignado");
        }
        return docente.getTipoVinculacion().getHorasMaximasSemanales() - calcularHorasAsignadas(idDocente, idPeriodo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Docente> listarPorTipoVinculacion(Integer idTipoVinculacion) {
        return docenteRepository.findByTipoVinculacion_IdTipoVinculacion(idTipoVinculacion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Docente> listarHabilitadosParaMateria(Integer idMateria) {
        return docenteRepository.findByMateriasHabilitadas_IdMateria(idMateria);
    }
}
