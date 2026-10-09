package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "RESTRICCION_HORARIA_DOCENTE")
public class RestriccionHorariaDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESTRICCION")
    private Integer idRestriccion;

    @Embedded
    private FranjaHoraria franja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCENTE", nullable = false)
    private Docente docente;

    public RestriccionHorariaDocente() {
    }

    public RestriccionHorariaDocente(FranjaHoraria franja) {
        this.franja = franja;
    }

    public Integer getIdRestriccion() {
        return idRestriccion;
    }

    public void setIdRestriccion(Integer idRestriccion) {
        this.idRestriccion = idRestriccion;
    }

    public FranjaHoraria getFranja() {
        return franja;
    }

    public void setFranja(FranjaHoraria franja) {
        this.franja = franja;
    }

    public Docente getDocente() {
        return docente;
    }

    void setDocente(Docente docente) {
        this.docente = docente;
    }
}
