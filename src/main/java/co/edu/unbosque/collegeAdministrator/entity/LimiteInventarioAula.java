package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import jakarta.persistence.*;

/**
 * ESP-F-02 y ESP-F-04: tope de aulas por tipo de espacio y sede, según el inventario de la Unidad de Espacios Físicos.
 */
@Entity
@Table(name = "LIMITE_INVENTARIO_AULA", uniqueConstraints = @UniqueConstraint(columnNames = {"TIPO_ESPACIO", "SEDE"}))
public class LimiteInventarioAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LIMITE")
    private Integer idLimite;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ESPACIO", nullable = false, length = 20)
    private TipoEspacio tipoEspacio;

    @Enumerated(EnumType.STRING)
    @Column(name = "SEDE", nullable = false, length = 20)
    private Sede sede;

    @Column(name = "MAXIMO", nullable = false)
    private Integer maximo;

    public LimiteInventarioAula() {
    }

    public LimiteInventarioAula(TipoEspacio tipoEspacio, Sede sede, Integer maximo) {
        this.tipoEspacio = tipoEspacio;
        this.sede = sede;
        this.maximo = maximo;
    }

    public Integer getIdLimite() {
        return idLimite;
    }

    public void setIdLimite(Integer idLimite) {
        this.idLimite = idLimite;
    }

    public TipoEspacio getTipoEspacio() {
        return tipoEspacio;
    }

    public void setTipoEspacio(TipoEspacio tipoEspacio) {
        this.tipoEspacio = tipoEspacio;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }
}
