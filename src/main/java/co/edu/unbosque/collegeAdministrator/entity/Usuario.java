package co.edu.unbosque.collegeAdministrator.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @Column(name = "ID_ROL")
    private Integer idRol;

    @Column(name = "INTENTOS")
    private Integer intentos;

    @Column(name = "FECHA_INIT_SESION")
    private LocalDateTime fechaInitSesion;

    public Usuario() {
    }

    public Usuario(Integer idUsuario, String username, String correo, String password, Integer idRol, Integer intentos, LocalDateTime fechaInitSesion) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.correo = correo;
        this.password = password;
        this.idRol = idRol;
        this.intentos = intentos;
        this.fechaInitSesion = fechaInitSesion;
    }

    public Integer getId() {
        return idUsuario;
    }

    public void setId(Integer idUsuario) {
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

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public LocalDateTime getFechaInitSesion() {
        return fechaInitSesion;
    }

    public void setFechaInitSesion(LocalDateTime fechaInitSesion) {
        this.fechaInitSesion = fechaInitSesion;
    }
}
