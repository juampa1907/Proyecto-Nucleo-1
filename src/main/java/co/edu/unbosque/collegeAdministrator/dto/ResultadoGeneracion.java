package co.edu.unbosque.collegeAdministrator.dto;

import java.util.List;

/**
 * HU-24: resultado de la propuesta automática. Para cada grupo no programado se indica el cuello de botella.
 */
public record ResultadoGeneracion(List<String> gruposProgramados, List<GrupoNoProgramado> gruposNoProgramados) {

    public record GrupoNoProgramado(Integer idGrupo, String grupo, String motivo) {
    }
}
