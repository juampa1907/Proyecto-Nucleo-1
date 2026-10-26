package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.dto.RespuestaLogin;


public interface AuthServiceAPI {

    RespuestaLogin login(String username, String password, String ip);


    void solicitarRecuperacion(String correo);

    void restablecerPassword(String token, String passwordNueva);
}
