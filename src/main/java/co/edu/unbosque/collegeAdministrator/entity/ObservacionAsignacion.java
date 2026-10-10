package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.EstadoObservacion;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "OBSERVACION_ASIGNACION")
public class ObservacionAsignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_OBSERVACION")
    private Integer idObservacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCENTE", nullable = false)
    private Docente docente;

    @Column(name = "TEXTO", nullable = false, length = 500)
    private String texto;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoObservacion estado = EstadoObservacion.PENDIENTE;

    public ObservacionAsignacion() {
    }

    public ObservacionAsignacion(Grupo grupo, Docente docente, String texto) {
        this.grupo = grupo;
        this.docente = docente;
        this.texto = texto;
        this.fecha = LocalDateTime.now();
    }

    public Integer getIdObservacion() {
        return idObservacion;
    }

    public void setIdObservacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public Docente getDocente() {
        return docente;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public EstadoObservacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoObservacion estado) {
        this.estado = estado;
    }
}
