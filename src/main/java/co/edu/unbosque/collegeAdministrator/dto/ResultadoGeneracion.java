package co.edu.unbosque.collegeAdministrator.dto;

import java.util.List;


public record ResultadoGeneracion(List<String> gruposProgramados, List<GrupoNoProgramado> gruposNoProgramados) {

    public record GrupoNoProgramado(Integer idGrupo, String grupo, String motivo) {
    }
}
