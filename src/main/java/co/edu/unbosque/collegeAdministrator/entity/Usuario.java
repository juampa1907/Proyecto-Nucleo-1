package co.edu.unbosque.collegeAdministrator.entity;

import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USUARIO")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "USERNAME", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "CORREO", unique = true, nullable = false, length = 255)
    private String correo;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @Column(name = "INTENTOS", nullable = false)
    private int intentos = 0;

    @Column(name = "FECHA_INIT_SESION")
    private LocalDateTime fechaInitSesion;

    @Column(name = "IP_ULTIMO_INICIO_SESION", length = 45)
    private String ipUltimoInicioSesion;

    @Column(name = "BLOQUEADO_HASTA")
    private LocalDateTime bloqueadoHasta;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "USUARIO_ROL", joinColumns = @JoinColumn(name = "ID_USUARIO"))
    @Enumerated(EnumType.STRING)
    @Column(name = "ROL", nullable = false, length = 30)
    private Set<Rol> roles = new HashSet<>();

    public Usuario() {
    }

    public Usuario(String username, String correo, String password) {
        this.username = username;
        this.correo = correo;
        this.password = password;
    }

    protected Usuario(Rol rolInicial) {
        this.roles.add(rolInicial);
    }

    protected Usuario(String username, String correo, String password, Rol rolInicial) {
        this(username, correo, password);
        this.roles.add(rolInicial);
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public LocalDateTime getFechaInitSesion() {
        return fechaInitSesion;
    }

    public void setFechaInitSesion(LocalDateTime fechaInitSesion) {
        this.fechaInitSesion = fechaInitSesion;
    }

    public String getIpUltimoInicioSesion() {
        return ipUltimoInicioSesion;
    }

    public void setIpUltimoInicioSesion(String ipUltimoInicioSesion) {
        this.ipUltimoInicioSesion = ipUltimoInicioSesion;
    }

    public LocalDateTime getBloqueadoHasta() {
        return bloqueadoHasta;
    }

    public void setBloqueadoHasta(LocalDateTime bloqueadoHasta) {
        this.bloqueadoHasta = bloqueadoHasta;
    }

    public Set<Rol> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public final void agregarRol(Rol rol) {
        roles.add(rol);
    }

    public final void quitarRol(Rol rol) {
        roles.remove(rol);
    }

    public final boolean tieneRol(Rol rol) {
        return roles.contains(rol);
    }
}
