package co.edu.unbosque.collegeAdministrator.controller;

import co.edu.unbosque.collegeAdministrator.dto.RespuestaLogin;
import co.edu.unbosque.collegeAdministrator.dto.request.LoginRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.RecuperarPasswordRequest;
import co.edu.unbosque.collegeAdministrator.dto.request.RestablecerPasswordRequest;
import co.edu.unbosque.collegeAdministrator.service.api.AuthServiceAPI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceAPI authService;

    public AuthController(AuthServiceAPI authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public RespuestaLogin login(@Valid @RequestBody LoginRequest request, HttpServletRequest peticion) {
        return authService.login(request.username(), request.password(), peticion.getRemoteAddr());
    }


    @PostMapping("/recuperar-password")
    public ResponseEntity<Void> recuperarPassword(@Valid @RequestBody RecuperarPasswordRequest request) {
        authService.solicitarRecuperacion(request.correo());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/restablecer-password")
    public ResponseEntity<Void> restablecerPassword(@Valid @RequestBody RestablecerPasswordRequest request) {
        authService.restablecerPassword(request.token(), request.passwordNueva());
        return ResponseEntity.noContent().build();
    }
}
