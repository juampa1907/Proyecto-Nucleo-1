package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TIPO_VINCULACION")
public class TipoVinculacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TIPO_VINCULACION")
    private Integer idTipoVinculacion;

    @Column(name = "NOMBRE", unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(name = "HORAS_MAXIMAS_SEMANALES", nullable = false)
    private Integer horasMaximasSemanales;

    public TipoVinculacion() {
    }

    public TipoVinculacion(String nombre, Integer horasMaximasSemanales) {
        this.nombre = nombre;
        this.horasMaximasSemanales = horasMaximasSemanales;
    }

    public Integer getIdTipoVinculacion() {
        return idTipoVinculacion;
    }

    public void setIdTipoVinculacion(Integer idTipoVinculacion) {
        this.idTipoVinculacion = idTipoVinculacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getHorasMaximasSemanales() {
        return horasMaximasSemanales;
    }

    public void setHorasMaximasSemanales(Integer horasMaximasSemanales) {
        this.horasMaximasSemanales = horasMaximasSemanales;
    }
}
