package co.edu.unbosque.collegeAdministrator.entity.enums;

public enum Bloque {
    A(Sede.USAQUEN),
    B(Sede.USAQUEN),
    E(Sede.USAQUEN),
    F(Sede.USAQUEN),
    G(Sede.USAQUEN),
    I(Sede.USAQUEN),
    J(Sede.USAQUEN),
    K(Sede.USAQUEN),
    L(Sede.USAQUEN),
    M(Sede.USAQUEN),
    N(Sede.USAQUEN),
    O(Sede.USAQUEN),
    Z(Sede.USAQUEN),
    HUB(Sede.USAQUEN),
    CENTRO_LENGUAS(Sede.USAQUEN),
    CENTRO_IMAGEN(Sede.USAQUEN),
    CHIA(Sede.CHIA);

    private final Sede sede;

    Bloque(Sede sede) {
        this.sede = sede;
    }

    public Sede getSede() {
        return sede;
    }
}
