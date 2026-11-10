package co.edu.unbosque.collegeAdministrator.dto;

import java.util.List;


/**
 * Resultado de generar la propuesta automática de horario.
 *
 * @param gruposProgramados las descripciones de los grupos programados por completo
 * @param gruposNoProgramados los grupos que no se pudieron completar y su motivo
 */
public record ResultadoGeneracion(List<String> gruposProgramados, List<GrupoNoProgramado> gruposNoProgramados) {

    /**
     * Grupo que no se pudo programar por completo.
     *
     * @param idGrupo el identificador del grupo
     * @param grupo la descripción del grupo
     * @param motivo la razón por la que no se completó
     */
    public record GrupoNoProgramado(Integer idGrupo, String grupo, String motivo) {
    }
}
