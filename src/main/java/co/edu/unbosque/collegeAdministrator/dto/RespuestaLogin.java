package co.edu.unbosque.collegeAdministrator.dto;

import java.util.List;

public record RespuestaLogin(String token, String username, List<String> roles) {
}
