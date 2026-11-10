package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA base de los usuarios de la aplicación, con sus credenciales, datos de sesión, estado de bloqueo
 * y roles.
 */
@Entity
@Table(name = "USUARIO")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    /** El identificador único del usuario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    /** El nombre de usuario único. */
    @Column(name = "USERNAME", unique = true, nullable = false, length = 50)
    private String username;

    /** El correo electrónico único del usuario. */
    @Column(name = "CORREO", unique = true, nullable = false, length = 255)
    private String correo;

    /** La contraseña del usuario, que se persiste cifrada. */
    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    /** El número de intentos fallidos de inicio de sesión acumulados. */
    @Column(name = "INTENTOS", nullable = false)
    private int intentos = 0;

    /** La fecha y hora del último inicio de sesión. */
    @Column(name = "FECHA_INIT_SESION")
    private LocalDateTime fechaInitSesion;

    /** La dirección IP del último inicio de sesión. */
    @Column(name = "IP_ULTIMO_INICIO_SESION", length = 45)
    private String ipUltimoInicioSesion;

    /** La fecha y hora hasta la que está bloqueada la cuenta, o {@code null} si no tiene bloqueo. */
    @Column(name = "BLOQUEADO_HASTA")
    private LocalDateTime bloqueadoHasta;

    /** Los roles asignados al usuario. */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "USUARIO_ROL", joinColumns = @JoinColumn(name = "ID_USUARIO"))
    @Enumerated(EnumType.STRING)
    @Column(name = "ROL", nullable = false, length = 30)
    private Set<Rol> roles = new HashSet<>();

    /**
     * Constructor vacío requerido por JPA.
     */
    public Usuario() {
    }

    /**
     * Crea una instancia de {@code Usuario} con los datos indicados.
     *
     * @param username el nombre de usuario único
     * @param correo el correo electrónico único del usuario
     * @param password la contraseña del usuario, que se persiste cifrada
     */
    public Usuario(String username, String correo, String password) {
        this.username = username;
        this.correo = correo;
        this.password = password;
    }

    /**
     * Crea un usuario vacío con un rol inicial; lo usan las subclases.
     *
     * @param rolInicial el rol con el que se crea el usuario
     */
    protected Usuario(Rol rolInicial) {
        this.roles.add(rolInicial);
    }

    /**
     * Crea un usuario con sus credenciales y un rol inicial; lo usan las subclases.
     *
     * @param username el nombre de usuario único
     * @param correo el correo electrónico único del usuario
     * @param password la contraseña del usuario, que se persiste cifrada
     * @param rolInicial el rol con el que se crea el usuario
     */
    protected Usuario(String username, String correo, String password, Rol rolInicial) {
        this(username, correo, password);
        this.roles.add(rolInicial);
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return el identificador único del usuario
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param idUsuario el identificador único del usuario
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre de usuario único.
     *
     * @return el nombre de usuario único
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario único.
     *
     * @param username el nombre de usuario único
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene el correo electrónico único del usuario.
     *
     * @return el correo electrónico único del usuario
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico único del usuario.
     *
     * @param correo el correo electrónico único del usuario
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contraseña del usuario, que se persiste cifrada.
     *
     * @return la contraseña del usuario, que se persiste cifrada
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario, que se persiste cifrada.
     *
     * @param password la contraseña del usuario, que se persiste cifrada
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el número de intentos fallidos de inicio de sesión acumulados.
     *
     * @return el número de intentos fallidos de inicio de sesión acumulados
     */
    public int getIntentos() {
        return intentos;
    }

    /**
     * Establece el número de intentos fallidos de inicio de sesión acumulados.
     *
     * @param intentos el número de intentos fallidos de inicio de sesión acumulados
     */
    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    /**
     * Obtiene la fecha y hora del último inicio de sesión.
     *
     * @return la fecha y hora del último inicio de sesión
     */
    public LocalDateTime getFechaInitSesion() {
        return fechaInitSesion;
    }

    /**
     * Establece la fecha y hora del último inicio de sesión.
     *
     * @param fechaInitSesion la fecha y hora del último inicio de sesión
     */
    public void setFechaInitSesion(LocalDateTime fechaInitSesion) {
        this.fechaInitSesion = fechaInitSesion;
    }

    /**
     * Obtiene la dirección IP del último inicio de sesión.
     *
     * @return la dirección IP del último inicio de sesión
     */
    public String getIpUltimoInicioSesion() {
        return ipUltimoInicioSesion;
    }

    /**
     * Establece la dirección IP del último inicio de sesión.
     *
     * @param ipUltimoInicioSesion la dirección IP del último inicio de sesión
     */
    public void setIpUltimoInicioSesion(String ipUltimoInicioSesion) {
        this.ipUltimoInicioSesion = ipUltimoInicioSesion;
    }

    /**
     * Obtiene la fecha y hora hasta la que está bloqueada la cuenta, o {@code null} si no tiene bloqueo.
     *
     * @return la fecha y hora hasta la que está bloqueada la cuenta, o {@code null} si no tiene bloqueo
     */
    public LocalDateTime getBloqueadoHasta() {
        return bloqueadoHasta;
    }

    /**
     * Establece la fecha y hora hasta la que está bloqueada la cuenta, o {@code null} si no tiene bloqueo.
     *
     * @param bloqueadoHasta la fecha y hora hasta la que está bloqueada la cuenta, o {@code null} si no tiene
     *         bloqueo
     */
    public void setBloqueadoHasta(LocalDateTime bloqueadoHasta) {
        this.bloqueadoHasta = bloqueadoHasta;
    }

    /**
     * Obtiene los roles del usuario.
     *
     * @return una vista no modificable de los roles
     */
    public Set<Rol> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    /**
     * Asigna un rol al usuario.
     *
     * @param rol el rol que se asigna
     */
    public final void agregarRol(Rol rol) {
        roles.add(rol);
    }

    /**
     * Retira un rol del usuario.
     *
     * @param rol el rol que se retira
     */
    public final void quitarRol(Rol rol) {
        roles.remove(rol);
    }

    /**
     * Indica si el usuario tiene un rol.
     *
     * @param rol el rol consultado
     * @return {@code true} si lo tiene
     */
    public final boolean tieneRol(Rol rol) {
        return roles.contains(rol);
    }
}
