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

@Service
@Transactional
public class EstudianteServiceImpl extends GenericServiceImpl<Estudiante, Integer> implements EstudianteServiceAPI {

    private final EstudianteRepository estudianteRepository;
    private final UsuarioServiceAPI usuarioService;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, UsuarioServiceAPI usuarioService) {
        this.estudianteRepository = estudianteRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    protected JpaRepository<Estudiante, Integer> getRepository() {
        return estudianteRepository;
    }

    @Override
    public Estudiante guardar(Estudiante estudiante) {
        if (estudiante.getIdUsuario() == null) {
            usuarioService.prepararNuevoUsuario(estudiante);
        }
        return super.guardar(estudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public Estudiante buscarPorUsername(String username) {
        return estudianteRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el estudiante " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Materia> listarMateriasActuales(Integer idEstudiante) {
        return buscarPorId(idEstudiante).getMateriasActuales();
    }

    @Override
    @Transactional(readOnly = true)
    public int calcularCreditosAprobados(Integer idEstudiante) {
        return buscarPorId(idEstudiante).getCreditosAprobados();
    }
}
