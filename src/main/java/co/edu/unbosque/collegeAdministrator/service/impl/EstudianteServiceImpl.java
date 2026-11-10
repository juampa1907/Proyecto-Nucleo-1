package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.Estudiante;
import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.repository.EstudianteRepository;
import co.edu.unbosque.collegeAdministrator.service.api.EstudianteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.UsuarioServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de las operaciones de negocio sobre los estudiantes.
 */
@Service
@Transactional
public class EstudianteServiceImpl extends GenericServiceImpl<Estudiante, Integer> implements EstudianteServiceAPI {

    /** El repositorio de persistencia de estudiantes. */
    private final EstudianteRepository estudianteRepository;
    /** El servicio de gestión de usuarios. */
    private final UsuarioServiceAPI usuarioService;

    /**
     * Crea una instancia de {@code EstudianteServiceImpl} con las dependencias requeridas.
     *
     * @param estudianteRepository el repositorio de persistencia de estudiantes
     * @param usuarioService el servicio de gestión de usuarios
     */
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, UsuarioServiceAPI usuarioService) {
        this.estudianteRepository = estudianteRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JpaRepository<Estudiante, Integer> getRepository() {
        return estudianteRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Al crear un estudiante valida sus datos y cifra su contraseña.
     */
    @Override
    public Estudiante guardar(Estudiante estudiante) {
        if (estudiante.getIdUsuario() == null) {
            usuarioService.prepararNuevoUsuario(estudiante);
        }
        return super.guardar(estudiante);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Estudiante buscarPorUsername(String username) {
        return estudianteRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el estudiante " + username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Materia> listarMateriasActuales(Integer idEstudiante) {
        return buscarPorId(idEstudiante).getMateriasActuales();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int calcularCreditosAprobados(Integer idEstudiante) {
        return buscarPorId(idEstudiante).getCreditosAprobados();
    }
}
