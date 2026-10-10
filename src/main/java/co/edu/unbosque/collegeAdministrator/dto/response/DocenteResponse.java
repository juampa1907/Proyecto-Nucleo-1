package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Escalafon;

import java.util.List;

/**
 * @param tipoVinculacion null si aún no se le asignó.
 */
public record DocenteResponse(Integer idUsuario, String username, String correo, Escalafon escalafon, String especialidad,
                              TipoVinculacionResponse tipoVinculacion, List<MateriaResumen> materiasHabilitadas,
                              List<RestriccionResponse> restriccionesHorarias) {
}
