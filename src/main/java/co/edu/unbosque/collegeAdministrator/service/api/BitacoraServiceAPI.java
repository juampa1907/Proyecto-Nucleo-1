package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.RegistroBitacora;

import java.time.LocalDateTime;
import java.util.List;


public interface BitacoraServiceAPI {

    void registrar(String accion, String detalle);


    List<RegistroBitacora> listar(LocalDateTime desde, LocalDateTime hasta, String username);
}
