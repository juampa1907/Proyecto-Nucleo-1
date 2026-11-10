package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad JPA que representa a un usuario estudiante y su historial de inscripciones.
 */
@Entity
@Table(name = "ESTUDIANTE")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public class Estudiante extends Usuario {

    /** El semestre que cursa el estudiante. */
    @Column(name = "NUMERO_SEMESTRE")
    private Integer numeroSemestre;

    /** El historial de inscripciones del estudiante. */
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InscripcionMateria> historialInscripciones = new ArrayList<>();

    /**
     * Crea un estudiante vacío con el rol {@code ESTUDIANTE}; requerido por JPA.
     */
    public Estudiante() {
        super(Rol.ESTUDIANTE);
    }

    /**
     * Crea un estudiante con el rol {@code ESTUDIANTE}.
     *
     * @param username el nombre de usuario
     * @param correo el correo electrónico del usuario
     * @param password la contraseña del usuario
     * @param numeroSemestre el semestre que cursa el estudiante
     */
    public Estudiante(String username, String correo, String password, Integer numeroSemestre) {
        super(username, correo, password, Rol.ESTUDIANTE);
        this.numeroSemestre = numeroSemestre;
    }

    /**
     * Obtiene el semestre que cursa el estudiante.
     *
     * @return el semestre que cursa el estudiante
     */
    public Integer getNumeroSemestre() {
        return numeroSemestre;
    }

    /**
     * Establece el semestre que cursa el estudiante.
     *
     * @param numeroSemestre el semestre que cursa el estudiante
     */
    public void setNumeroSemestre(Integer numeroSemestre) {
        this.numeroSemestre = numeroSemestre;
    }

    /**
     * Obtiene el historial de inscripciones del estudiante.
     *
     * @return una vista no modificable del historial
     */
    public List<InscripcionMateria> getHistorialInscripciones() {
        return Collections.unmodifiableList(historialInscripciones);
    }

    /**
     * Agrega una inscripción al historial y la vincula con este estudiante.
     *
     * @param inscripcion la inscripción que se agrega
     */
    public void agregarInscripcion(InscripcionMateria inscripcion) {
        historialInscripciones.add(inscripcion);
        inscripcion.setEstudiante(this);
    }

    /**
     * Quita una inscripción del historial y elimina su vínculo con este estudiante.
     *
     * @param inscripcion la inscripción que se quita
     */
    public void quitarInscripcion(InscripcionMateria inscripcion) {
        historialInscripciones.remove(inscripcion);
        inscripcion.setEstudiante(null);
    }

    /**
     * Obtiene las materias que el estudiante está cursando.
     *
     * @return las materias en estado {@code CURSANDO}
     */
    public List<Materia> getMateriasActuales() {
        return materiasConEstado(EstadoMateria.CURSANDO);
    }

    /**
     * Obtiene las materias que el estudiante ya aprobó.
     *
     * @return las materias en estado {@code APROBADA}
     */
    public List<Materia> getMateriasCursadas() {
        return materiasConEstado(EstadoMateria.APROBADA);
    }

    /**
     * Calcula los créditos aprobados por el estudiante.
     *
     * @return la suma de créditos de las materias aprobadas
     */
    public int getCreditosAprobados() {
        return getMateriasCursadas().stream()
                .mapToInt(Materia::getNumeroCreditos)
                .sum();
    }

    /**
     * Filtra las materias del historial según su estado.
     *
     * @param estado el estado buscado
     * @return las materias de las inscripciones con ese estado
     */
    private List<Materia> materiasConEstado(EstadoMateria estado) {
        return historialInscripciones.stream()
                .filter(ins -> ins.getEstado() == estado)
                .map(ins -> ins.getGrupo().getMateria())
                .toList();
    }
}
