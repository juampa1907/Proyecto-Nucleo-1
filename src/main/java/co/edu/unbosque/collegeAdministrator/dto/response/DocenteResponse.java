package co.edu.unbosque.collegeAdministrator.dto.response;

import co.edu.unbosque.collegeAdministrator.entity.enums.Escalafon;

import java.util.List;


/**
 * Datos de un docente.
 *
 * @param idUsuario el identificador del usuario
 * @param username el nombre de usuario
 * @param correo el correo electrónico del usuario
 * @param escalafon el escalafón del docente
 * @param especialidad la especialidad del docente
 * @param tipoVinculacion el tipo de vinculación, o {@code null} si no tiene
 * @param materiasHabilitadas las materias que puede dictar
 * @param restriccionesHorarias sus restricciones horarias
 */
public record DocenteResponse(Integer idUsuario, String username, String correo, Escalafon escalafon, String especialidad,
                              TipoVinculacionResponse tipoVinculacion, List<MateriaResumen> materiasHabilitadas,
                              List<RestriccionResponse> restriccionesHorarias) {
}
