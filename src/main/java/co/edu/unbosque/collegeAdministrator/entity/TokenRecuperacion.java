package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * HU-03: enlace de recuperación de contraseña. Se usa una sola vez y vence a los 30 minutos.
 */
@Entity
@Table(name = "TOKEN_RECUPERACION")
public class TokenRecuperacion {

    /** Minutos de vigencia de un token desde su creación. */
    public static final int MINUTOS_VIGENCIA = 30;

    /** El identificador único del registro. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TOKEN")
    private Integer idToken;

    /** El valor único del token enviado al usuario. */
    @Column(name = "TOKEN", unique = true, nullable = false, length = 36)
    private String token;

    /** El usuario que solicitó la recuperación. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    /** La fecha y hora de vencimiento del token. */
    @Column(name = "EXPIRA_EN", nullable = false)
    private LocalDateTime expiraEn;

    /** Indica si el token ya fue utilizado. */
    @Column(name = "USADO", nullable = false)
    private boolean usado;

    /**
     * Constructor vacío requerido por JPA.
     */
    public TokenRecuperacion() {
    }

    /**
     * Crea un token que vence {@link #MINUTOS_VIGENCIA} minutos después del instante indicado.
     *
     * @param token el valor único del token enviado al usuario
     * @param usuario el usuario que solicitó la recuperación
     * @param ahora el instante de creación del token
     */
    public TokenRecuperacion(String token, Usuario usuario, LocalDateTime ahora) {
        this.token = token;
        this.usuario = usuario;
        this.expiraEn = ahora.plusMinutes(MINUTOS_VIGENCIA);
    }

    /**
     * Indica si el token puede usarse.
     *
     * @param ahora el instante evaluado
     * @return {@code true} si no se ha usado y no ha vencido
     */
    public boolean estaVigente(LocalDateTime ahora) {
        return !usado && ahora.isBefore(expiraEn);
    }

    /**
     * Obtiene el identificador único del registro.
     *
     * @return el identificador único del registro
     */
    public Integer getIdToken() {
        return idToken;
    }

    /**
     * Obtiene el valor único del token enviado al usuario.
     *
     * @return el valor único del token enviado al usuario
     */
    public String getToken() {
        return token;
    }

    /**
     * Obtiene el usuario que solicitó la recuperación.
     *
     * @return el usuario que solicitó la recuperación
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Obtiene la fecha y hora de vencimiento del token.
     *
     * @return la fecha y hora de vencimiento del token
     */
    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    /**
     * Indica si el token ya fue utilizado.
     *
     * @return {@code true} si el token ya fue utilizado
     */
    public boolean isUsado() {
        return usado;
    }

    /**
     * Establece si el token ya fue utilizado.
     *
     * @param usado {@code true} si el token ya fue utilizado
     */
    public void setUsado(boolean usado) {
        this.usado = usado;
    }
}
