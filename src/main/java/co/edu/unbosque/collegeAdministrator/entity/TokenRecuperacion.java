package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * HU-03: enlace de recuperación de contraseña. Se usa una sola vez y vence a los 30 minutos.
 */
@Entity
@Table(name = "TOKEN_RECUPERACION")
public class TokenRecuperacion {

    public static final int MINUTOS_VIGENCIA = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TOKEN")
    private Integer idToken;

    @Column(name = "TOKEN", unique = true, nullable = false, length = 36)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "EXPIRA_EN", nullable = false)
    private LocalDateTime expiraEn;

    @Column(name = "USADO", nullable = false)
    private boolean usado;

    public TokenRecuperacion() {
    }

    public TokenRecuperacion(String token, Usuario usuario, LocalDateTime ahora) {
        this.token = token;
        this.usuario = usuario;
        this.expiraEn = ahora.plusMinutes(MINUTOS_VIGENCIA);
    }

    public boolean estaVigente(LocalDateTime ahora) {
        return !usado && ahora.isBefore(expiraEn);
    }

    public Integer getIdToken() {
        return idToken;
    }

    public String getToken() {
        return token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }
}
