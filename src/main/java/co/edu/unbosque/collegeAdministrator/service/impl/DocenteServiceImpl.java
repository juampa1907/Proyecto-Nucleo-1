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

@Service
@Transactional
public class DocenteServiceImpl extends GenericServiceImpl<Docente, Integer> implements DocenteServiceAPI {

    private final DocenteRepository docenteRepository;
    private final GrupoRepository grupoRepository;
    private final UsuarioServiceAPI usuarioService;
    private final TipoVinculacionServiceAPI tipoVinculacionService;
    private final MateriaServiceAPI materiaService;

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

    @Override
    protected JpaRepository<Docente, Integer> getRepository() {
        return docenteRepository;
    }

    @Override
    public Docente guardar(Docente docente) {
        if (docente.getIdUsuario() == null) {
            usuarioService.prepararNuevoUsuario(docente);
        }
        return super.guardar(docente);
    }

    @Override
    @Transactional(readOnly = true)
    public Docente buscarPorUsername(String username) {
        return docenteRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el docente " + username));
    }

    @Override
    public Docente asignarTipoVinculacion(Integer idDocente, Integer idTipoVinculacion) {
        Docente docente = buscarPorId(idDocente);
        docente.setTipoVinculacion(tipoVinculacionService.buscarPorId(idTipoVinculacion));
        return docente;
    }

    @Override
    public Docente habilitarMateria(Integer idDocente, Integer idMateria) {
        Docente docente = buscarPorId(idDocente);
        docente.habilitarMateria(materiaService.buscarPorId(idMateria));
        return docente;
    }

    @Override
    public Docente deshabilitarMateria(Integer idDocente, Integer idMateria) {
        Docente docente = buscarPorId(idDocente);
        docente.deshabilitarMateria(materiaService.buscarPorId(idMateria));
        return docente;
    }

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

    @Override
    @Transactional(readOnly = true)
    public double calcularHorasAsignadas(Integer idDocente, Integer idPeriodo) {
        buscarPorId(idDocente);
        return grupoRepository.findByDocente_IdUsuarioAndPeriodo_IdPeriodo(idDocente, idPeriodo).stream()
                .filter(grupo -> grupo.getEstado() != EstadoGrupo.CERRADO)
                .mapToDouble(Grupo::calcularHorasSemanales)
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public double calcularHorasDisponibles(Integer idDocente, Integer idPeriodo) {
        Docente docente = buscarPorId(idDocente);
        if (docente.getTipoVinculacion() == null) {
            throw new ReglaNegocioException("El docente " + docente.getUsername() + " no tiene tipo de vinculación asignado");
        }
        return docente.getTipoVinculacion().getHorasMaximasSemanales() - calcularHorasAsignadas(idDocente, idPeriodo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Docente> listarPorTipoVinculacion(Integer idTipoVinculacion) {
        return docenteRepository.findByTipoVinculacion_IdTipoVinculacion(idTipoVinculacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Docente> listarHabilitadosParaMateria(Integer idMateria) {
        return docenteRepository.findByMateriasHabilitadas_IdMateria(idMateria);
    }
}
