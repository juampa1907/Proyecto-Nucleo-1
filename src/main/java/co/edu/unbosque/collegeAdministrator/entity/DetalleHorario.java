package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DETALLE_HORARIO")
public class DetalleHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_HORARIO")
    private Integer idDetalleHorario;

    @Embedded
    private FranjaHoraria franja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA")
    private Aula aula;

    // ESP-F-05: los auditorios solo se asignan de forma manual y marcada como excepcional.
    @Column(name = "USO_EXCEPCIONAL", nullable = false)
    private boolean usoExcepcional;

    public DetalleHorario() {
    }

    public DetalleHorario(FranjaHoraria franja, Aula aula, boolean usoExcepcional) {
        this.franja = franja;
        this.aula = aula;
        this.usoExcepcional = usoExcepcional;
    }

    public boolean isUsoExcepcional() {
        return usoExcepcional;
    }

    public Integer getIdDetalleHorario() {
        return idDetalleHorario;
    }

    public void setIdDetalleHorario(Integer idDetalleHorario) {
        this.idDetalleHorario = idDetalleHorario;
    }

    public FranjaHoraria getFranja() {
        return franja;
    }

    public void setFranja(FranjaHoraria franja) {
        this.franja = franja;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }
}
