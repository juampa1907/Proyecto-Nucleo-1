package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Usuario;
import co.edu.unbosque.collegeAdministrator.entity.enums.Rol;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

/**
 * Operaciones de negocio sobre los usuarios, sus roles y sus credenciales.
 */
public interface UsuarioServiceAPI extends GenericServiceAPI<Usuario, Integer> {

    /**
     * Busca un usuario por nombre de usuario.
     *
     * @param username el nombre de usuario
     * @return el usuario encontrado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.RecursoNoEncontradoException si no existe
     */
    Usuario buscarPorUsername(String username);

    /**
     * Verifica que el nombre de usuario y el correo no estén registrados.
     *
     * @param username el nombre de usuario
     * @param correo el correo electrónico del usuario
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si alguno ya existe
     */
    void validarDatosUnicos(String username, String correo);


    /**
     * Valida los datos de un usuario nuevo y cifra su contraseña.
     *
     * @param usuario el usuario que se prepara
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si los datos no son
     *         válidos
     */
    void prepararNuevoUsuario(Usuario usuario);

    /**
     * Crea un usuario con el rol de administrador.
     *
     * @param username el nombre de usuario
     * @param correo el correo electrónico del usuario
     * @param password la contraseña del usuario
     * @return el administrador creado
     */
    Usuario crearAdministrador(String username, String correo, String password);

    /**
     * Asigna un rol a un usuario.
     *
     * @param idUsuario el identificador del usuario
     * @param rol el rol del usuario
     * @return el usuario actualizado
     */
    Usuario asignarRol(Integer idUsuario, Rol rol);

    /**
     * Retira un rol de un usuario.
     *
     * @param idUsuario el identificador del usuario
     * @param rol el rol del usuario
     * @return el usuario actualizado
     */
    Usuario quitarRol(Integer idUsuario, Rol rol);

    /**
     * Cambia la contraseña de un usuario verificando la actual.
     *
     * @param idUsuario el identificador del usuario
     * @param passwordActual la contraseña vigente del usuario
     * @param passwordNueva la nueva contraseña
     * @return el usuario actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si la contraseña
     *         actual es incorrecta o la nueva no es válida
     */
    Usuario cambiarPassword(Integer idUsuario, String passwordActual, String passwordNueva);

    /**
     * Verifica que una contraseña cumpla la longitud mínima.
     *
     * @param password la contraseña evaluada
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si es demasiado
     *         corta o nula
     */
    void validarPassword(String password);

    /**
     * Reinicia los intentos fallidos y guarda la fecha y la IP del inicio de sesión.
     *
     * @param username el nombre de usuario
     * @param ip la dirección IP del inicio de sesión
     * @return el usuario actualizado
     * @throws co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException si el usuario está
     *         bloqueado
     */
    Usuario registrarInicioSesion(String username, String ip);

    /**
     * Cuenta un intento fallido y bloquea temporalmente la cuenta al llegar al máximo.
     *
     * @param username el nombre de usuario
     * @return el usuario actualizado
     */
    Usuario registrarIntentoFallido(String username);

    /**
     * Indica si un usuario tiene un bloqueo vigente.
     *
     * @param usuario el usuario evaluado
     * @return {@code true} si el bloqueo sigue vigente
     */
    boolean estaBloqueado(Usuario usuario);
}
