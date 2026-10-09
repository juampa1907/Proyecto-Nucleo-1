package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoMateria;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "ESTUDIANTE")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public class Estudiante extends Usuario {

    @Column(name = "NUMERO_SEMESTRE")
    private Integer numeroSemestre;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InscripcionMateria> historialInscripciones = new ArrayList<>();

    public Estudiante() {
        super(Rol.ESTUDIANTE);
    }

    public Estudiante(String username, String correo, String password, Integer numeroSemestre) {
        super(username, correo, password, Rol.ESTUDIANTE);
        this.numeroSemestre = numeroSemestre;
    }

    public Integer getNumeroSemestre() {
        return numeroSemestre;
    }

    public void setNumeroSemestre(Integer numeroSemestre) {
        this.numeroSemestre = numeroSemestre;
    }

    public List<InscripcionMateria> getHistorialInscripciones() {
        return Collections.unmodifiableList(historialInscripciones);
    }

    public void agregarInscripcion(InscripcionMateria inscripcion) {
        historialInscripciones.add(inscripcion);
        inscripcion.setEstudiante(this);
    }

    public void quitarInscripcion(InscripcionMateria inscripcion) {
        historialInscripciones.remove(inscripcion);
        inscripcion.setEstudiante(null);
    }

    public List<Materia> getMateriasActuales() {
        return materiasConEstado(EstadoMateria.CURSANDO);
    }

    public List<Materia> getMateriasCursadas() {
        return materiasConEstado(EstadoMateria.APROBADA);
    }

    public int getCreditosAprobados() {
        return getMateriasCursadas().stream()
                .mapToInt(Materia::getNumeroCreditos)
                .sum();
    }

    private List<Materia> materiasConEstado(EstadoMateria estado) {
        return historialInscripciones.stream()
                .filter(ins -> ins.getEstado() == estado)
                .map(ins -> ins.getGrupo().getMateria())
                .toList();
    }
}
