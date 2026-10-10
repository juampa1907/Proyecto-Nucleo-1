package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.Materia;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.repository.MateriaRepository;
import co.edu.unbosque.collegeAdministrator.service.api.MateriaServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceImpl;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class MateriaServiceImpl extends GenericServiceImpl<Materia, Integer> implements MateriaServiceAPI {

    private final MateriaRepository materiaRepository;

    public MateriaServiceImpl(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    @Override
    protected JpaRepository<Materia, Integer> getRepository() {
        return materiaRepository;
    }

    @Override
    public Materia guardar(Materia materia) {
        if (materia.getCodigo() == null || materia.getCodigo().isBlank()) {
            throw new ReglaNegocioException("El código de la materia es obligatorio");
        }
        if (materia.getNumeroCreditos() == null || materia.getNumeroCreditos() <= 0) {
            throw new ReglaNegocioException("Los créditos de la materia deben ser mayores a 0");
        }
        if (materia.getHorasSemanales() == null || materia.getHorasSemanales() <= 0) {
            throw new ReglaNegocioException("Las horas semanales de la materia deben ser mayores a 0");
        }
        if (materia.getIdMateria() == null) {
            if (materiaRepository.existsByCodigoIgnoreCase(materia.getCodigo())) {
                throw new ReglaNegocioException("Ya existe una materia con el código " + materia.getCodigo());
            }
            if (materiaRepository.existsByNombreIgnoreCase(materia.getNombre())) {
                throw new ReglaNegocioException("Ya existe la materia " + materia.getNombre());
            }
        }
        return super.guardar(materia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Materia> buscarPorNombre(String nombre) {
        return materiaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Materia agregarPrerrequisito(Integer idMateria, Integer idPrerrequisito) {
        validarDistintas(idMateria, idPrerrequisito, "prerrequisito");
        Materia materia = buscarPorId(idMateria);
        Materia prerrequisito = buscarPorId(idPrerrequisito);
        if (contieneMateria(materia.getPrerrequisitos(), idPrerrequisito)) {
            throw new ReglaNegocioException(prerrequisito.getNombre() + " ya es prerrequisito de " + materia.getNombre());
        }
        if (contieneMateria(materia.getCorrequisitos(), idPrerrequisito)) {
            throw new ReglaNegocioException(prerrequisito.getNombre() + " ya es correquisito de " + materia.getNombre());
        }
        if (contieneMateria(prerrequisito.getPrerrequisitos(), idMateria)) {
            throw new ReglaNegocioException("Ciclo de prerrequisitos: " + materia.getNombre()
                    + " ya es prerrequisito de " + prerrequisito.getNombre());
        }
        materia.agregarPrerrequisito(prerrequisito);
        return materia;
    }

    @Override
    public Materia quitarPrerrequisito(Integer idMateria, Integer idPrerrequisito) {
        Materia materia = buscarPorId(idMateria);
        Materia prerrequisito = buscarEn(materia.getPrerrequisitos(), idPrerrequisito, "prerrequisito", materia);
        materia.quitarPrerrequisito(prerrequisito);
        return materia;
    }

    // HU-15: un correquisito se cursa en el mismo periodo o ya debe estar aprobado.
    @Override
    public Materia agregarCorrequisito(Integer idMateria, Integer idCorrequisito) {
        validarDistintas(idMateria, idCorrequisito, "correquisito");
        Materia materia = buscarPorId(idMateria);
        Materia correquisito = buscarPorId(idCorrequisito);
        if (contieneMateria(materia.getCorrequisitos(), idCorrequisito)) {
            throw new ReglaNegocioException(correquisito.getNombre() + " ya es correquisito de " + materia.getNombre());
        }
        if (contieneMateria(materia.getPrerrequisitos(), idCorrequisito)) {
            throw new ReglaNegocioException(correquisito.getNombre() + " ya es prerrequisito de " + materia.getNombre());
        }
        materia.agregarCorrequisito(correquisito);
        return materia;
    }

    @Override
    public Materia quitarCorrequisito(Integer idMateria, Integer idCorrequisito) {
        Materia materia = buscarPorId(idMateria);
        Materia correquisito = buscarEn(materia.getCorrequisitos(), idCorrequisito, "correquisito", materia);
        materia.quitarCorrequisito(correquisito);
        return materia;
    }

    // HU-13 / RF-15: requisitos especiales de aula.
    @Override
    public Materia agregarRequisitoAula(Integer idMateria, CaracteristicaAula requisito) {
        if (requisito == null) {
            throw new ReglaNegocioException("El requisito de aula es obligatorio");
        }
        Materia materia = buscarPorId(idMateria);
        materia.agregarRequisitoAula(requisito);
        return materia;
    }

    @Override
    public Materia quitarRequisitoAula(Integer idMateria, CaracteristicaAula requisito) {
        Materia materia = buscarPorId(idMateria);
        materia.quitarRequisitoAula(requisito);
        return materia;
    }

    private void validarDistintas(Integer idMateria, Integer idOtra, String relacion) {
        if (idMateria.equals(idOtra)) {
            throw new ReglaNegocioException("Una materia no puede ser " + relacion + " de sí misma");
        }
    }

    private boolean contieneMateria(Collection<Materia> materias, Integer idMateria) {
        return materias.stream().anyMatch(m -> m.getIdMateria().equals(idMateria));
    }

    private Materia buscarEn(Collection<Materia> materias, Integer idBuscada, String relacion, Materia materia) {
        return materias.stream()
                .filter(m -> m.getIdMateria().equals(idBuscada))
                .findFirst()
                .orElseThrow(() -> new ReglaNegocioException(
                        "La materia con id " + idBuscada + " no es " + relacion + " de " + materia.getNombre()));
    }
}
