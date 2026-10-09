package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.Escalafon;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "DOCENTE")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public class Docente extends Usuario {

    @Enumerated(EnumType.STRING)
    @Column(name = "ESCALAFON", length = 20)
    private Escalafon escalafon;

    @Column(name = "ESPECIALIDAD", length = 100)
    private String especialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_VINCULACION")
    private TipoVinculacion tipoVinculacion;

    @ManyToMany
    @JoinTable(
            name = "DOCENTE_MATERIA_HABILITADA",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_MATERIA")
    )
    private Set<Materia> materiasHabilitadas = new HashSet<>();

    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestriccionHorariaDocente> restriccionesHorarias = new ArrayList<>();

    public Docente() {
        super(Rol.DOCENTE);
    }

    public Docente(String username, String correo, String password) {
        super(username, correo, password, Rol.DOCENTE);
    }

    public Escalafon getEscalafon() {
        return escalafon;
    }

    public void setEscalafon(Escalafon escalafon) {
        this.escalafon = escalafon;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public TipoVinculacion getTipoVinculacion() {
        return tipoVinculacion;
    }

    public void setTipoVinculacion(TipoVinculacion tipoVinculacion) {
        this.tipoVinculacion = tipoVinculacion;
    }

    public Set<Materia> getMateriasHabilitadas() {
        return Collections.unmodifiableSet(materiasHabilitadas);
    }

    public void habilitarMateria(Materia materia) {
        materiasHabilitadas.add(materia);
    }

    public void deshabilitarMateria(Materia materia) {
        materiasHabilitadas.remove(materia);
    }

    public List<RestriccionHorariaDocente> getRestriccionesHorarias() {
        return Collections.unmodifiableList(restriccionesHorarias);
    }

    public void agregarRestriccion(RestriccionHorariaDocente restriccion) {
        restriccionesHorarias.add(restriccion);
        restriccion.setDocente(this);
    }

    public void quitarRestriccion(RestriccionHorariaDocente restriccion) {
        restriccionesHorarias.remove(restriccion);
        restriccion.setDocente(null);
    }
}
