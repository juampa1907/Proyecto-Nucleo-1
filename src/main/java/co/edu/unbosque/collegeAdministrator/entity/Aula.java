package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.Bloque;
import co.edu.unbosque.collegeAdministrator.entity.enums.CaracteristicaAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AULA", uniqueConstraints = @UniqueConstraint(columnNames = {"CODIGO", "BLOQUE"}))
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA")
    private Integer idAula;

    @Column(name = "CODIGO", nullable = false, length = 20)
    private String codigo;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ESPACIO", nullable = false, length = 20)
    private TipoEspacio tipoEspacio;

    @ElementCollection
    @CollectionTable(name = "AULA_CARACTERISTICA", joinColumns = @JoinColumn(name = "ID_AULA"))
    @Enumerated(EnumType.STRING)
    @Column(name = "CARACTERISTICA", nullable = false, length = 30)
    private Set<CaracteristicaAula> caracteristicas = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "BLOQUE", nullable = false, length = 20)
    private Bloque bloque;

    @Column(name = "ACTIVA", nullable = false)
    private boolean activa = true;

    public Aula() {
    }

    public Aula(String codigo, Integer capacidad, TipoEspacio tipoEspacio, Bloque bloque) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.tipoEspacio = tipoEspacio;
        this.bloque = bloque;
    }

    public Integer getIdAula() {
        return idAula;
    }

    public void setIdAula(Integer idAula) {
        this.idAula = idAula;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public TipoEspacio getTipoEspacio() {
        return tipoEspacio;
    }

    public void setTipoEspacio(TipoEspacio tipoEspacio) {
        this.tipoEspacio = tipoEspacio;
    }

    public Set<CaracteristicaAula> getCaracteristicas() {
        return Collections.unmodifiableSet(caracteristicas);
    }

    public void agregarCaracteristica(CaracteristicaAula caracteristica) {
        caracteristicas.add(caracteristica);
    }

    public void quitarCaracteristica(CaracteristicaAula caracteristica) {
        caracteristicas.remove(caracteristica);
    }

    public boolean tieneCaracteristica(CaracteristicaAula caracteristica) {
        return caracteristicas.contains(caracteristica);
    }

    public Bloque getBloque() {
        return bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
