package co.edu.unbosque.collegeAdministrator.entity.enums;

/**
 * Bloques o edificios de la universidad, cada uno asociado a su sede.
 */
public enum Bloque {
    /** Bloque A de la sede Usaquén. */
    A(Sede.USAQUEN),
    /** Bloque B de la sede Usaquén. */
    B(Sede.USAQUEN),
    /** Bloque E de la sede Usaquén. */
    E(Sede.USAQUEN),
    /** Bloque F de la sede Usaquén. */
    F(Sede.USAQUEN),
    /** Bloque G de la sede Usaquén. */
    G(Sede.USAQUEN),
    /** Bloque I de la sede Usaquén. */
    I(Sede.USAQUEN),
    /** Bloque J de la sede Usaquén. */
    J(Sede.USAQUEN),
    /** Bloque K de la sede Usaquén. */
    K(Sede.USAQUEN),
    /** Bloque L de la sede Usaquén. */
    L(Sede.USAQUEN),
    /** Bloque M de la sede Usaquén. */
    M(Sede.USAQUEN),
    /** Bloque N de la sede Usaquén. */
    N(Sede.USAQUEN),
    /** Bloque O de la sede Usaquén. */
    O(Sede.USAQUEN),
    /** Bloque Z de la sede Usaquén. */
    Z(Sede.USAQUEN),
    /** Bloque HUB de la sede Usaquén. */
    HUB(Sede.USAQUEN),
    /** Centro de Lenguas de la sede Usaquén. */
    CENTRO_LENGUAS(Sede.USAQUEN),
    /** Centro de Imagen de la sede Usaquén. */
    CENTRO_IMAGEN(Sede.USAQUEN),
    /** Bloque de la sede Chía. */
    CHIA(Sede.CHIA);

    /** La sede a la que pertenece el bloque. */
    private final Sede sede;

    /**
     * Asocia el bloque con su sede.
     *
     * @param sede la sede a la que pertenece el bloque
     */
    Bloque(Sede sede) {
        this.sede = sede;
    }

    /**
     * Obtiene la sede del bloque.
     *
     * @return la sede a la que pertenece el bloque
     */
    public Sede getSede() {
        return sede;
    }
}
