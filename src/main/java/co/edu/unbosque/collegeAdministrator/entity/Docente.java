package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.Escalafon;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad JPA que representa a un usuario docente, con su vinculación, materias habilitadas y restricciones
 * horarias.
 */
@Entity
@Table(name = "DOCENTE")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public class Docente extends Usuario {

    /** El escalafón del docente. */
    @Enumerated(EnumType.STRING)
    @Column(name = "ESCALAFON", length = 20)
    private Escalafon escalafon;

    /** La especialidad del docente. */
    @Column(name = "ESPECIALIDAD", length = 100)
    private String especialidad;

    /** El tipo de vinculación del docente, que determina sus horas máximas semanales. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_VINCULACION")
    private TipoVinculacion tipoVinculacion;

    /** Las materias que el docente puede dictar. */
    @ManyToMany
    @JoinTable(
            name = "DOCENTE_MATERIA_HABILITADA",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_MATERIA")
    )
    private Set<Materia> materiasHabilitadas = new HashSet<>();

    /** Las franjas en las que el docente no puede dictar clase. */
    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestriccionHorariaDocente> restriccionesHorarias = new ArrayList<>();

    /**
     * Crea un docente vacío con el rol {@code DOCENTE}; requerido por JPA.
     */
    public Docente() {
        super(Rol.DOCENTE);
    }

    /**
     * Crea un docente con el rol {@code DOCENTE}.
     *
     * @param username el nombre de usuario
     * @param correo el correo electrónico del usuario
     * @param password la contraseña del usuario
     */
    public Docente(String username, String correo, String password) {
        super(username, correo, password, Rol.DOCENTE);
    }

    /**
     * Obtiene el escalafón del docente.
     *
     * @return el escalafón del docente
     */
    public Escalafon getEscalafon() {
        return escalafon;
    }

    /**
     * Establece el escalafón del docente.
     *
     * @param escalafon el escalafón del docente
     */
    public void setEscalafon(Escalafon escalafon) {
        this.escalafon = escalafon;
    }

    /**
     * Obtiene la especialidad del docente.
     *
     * @return la especialidad del docente
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Establece la especialidad del docente.
     *
     * @param especialidad la especialidad del docente
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Obtiene el tipo de vinculación del docente, que determina sus horas máximas semanales.
     *
     * @return el tipo de vinculación del docente, que determina sus horas máximas semanales
     */
    public TipoVinculacion getTipoVinculacion() {
        return tipoVinculacion;
    }

    /**
     * Establece el tipo de vinculación del docente, que determina sus horas máximas semanales.
     *
     * @param tipoVinculacion el tipo de vinculación del docente, que determina sus horas máximas semanales
     */
    public void setTipoVinculacion(TipoVinculacion tipoVinculacion) {
        this.tipoVinculacion = tipoVinculacion;
    }

    /**
     * Obtiene las materias que el docente puede dictar.
     *
     * @return una vista no modificable de las materias habilitadas
     */
    public Set<Materia> getMateriasHabilitadas() {
        return Collections.unmodifiableSet(materiasHabilitadas);
    }

    /**
     * Habilita al docente para dictar una materia.
     *
     * @param materia la materia que se habilita
     */
    public void habilitarMateria(Materia materia) {
        materiasHabilitadas.add(materia);
    }

    /**
     * Retira la habilitación del docente para dictar una materia.
     *
     * @param materia la materia que se deshabilita
     */
    public void deshabilitarMateria(Materia materia) {
        materiasHabilitadas.remove(materia);
    }

    /**
     * Obtiene las restricciones horarias del docente.
     *
     * @return una vista no modificable de las restricciones
     */
    public List<RestriccionHorariaDocente> getRestriccionesHorarias() {
        return Collections.unmodifiableList(restriccionesHorarias);
    }

    /**
     * Agrega una restricción horaria y la vincula con este docente.
     *
     * @param restriccion la restricción que se agrega
     */
    public void agregarRestriccion(RestriccionHorariaDocente restriccion) {
        restriccionesHorarias.add(restriccion);
        restriccion.setDocente(this);
    }

    /**
     * Quita una restricción horaria y elimina su vínculo con este docente.
     *
     * @param restriccion la restricción que se quita
     */
    public void quitarRestriccion(RestriccionHorariaDocente restriccion) {
        restriccionesHorarias.remove(restriccion);
        restriccion.setDocente(null);
    }
}
