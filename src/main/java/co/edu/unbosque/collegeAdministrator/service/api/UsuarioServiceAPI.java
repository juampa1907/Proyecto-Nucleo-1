package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Usuario;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

public interface UsuarioServiceAPI extends GenericServiceAPI<Usuario, Integer> {

    Usuario buscarPorUsername(String username);

    void validarDatosUnicos(String username, String correo);


    void prepararNuevoUsuario(Usuario usuario);

    Usuario crearAdministrador(String username, String correo, String password);

    Usuario asignarRol(Integer idUsuario, Rol rol);

    Usuario quitarRol(Integer idUsuario, Rol rol);

    Usuario cambiarPassword(Integer idUsuario, String passwordActual, String passwordNueva);

    void validarPassword(String password);

    Usuario registrarInicioSesion(String username, String ip);

    Usuario registrarIntentoFallido(String username);

    boolean estaBloqueado(Usuario usuario);
}
