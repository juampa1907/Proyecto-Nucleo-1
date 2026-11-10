package co.edu.unbosque.collegeAdministrator.dto.reporte;


/**
 * Clasificación de la carga horaria de un docente frente a su máximo.
 */
public enum IndicadorCarga {
    /** La carga está por debajo del umbral de subutilización. */
    SUBUTILIZADO,
    /** La carga está entre el umbral de subutilización y el máximo. */
    ADECUADO,
    /** La carga supera las horas máximas del docente. */
    SOBREASIGNADO,
    /** El docente no tiene tipo de vinculación. */
    SIN_VINCULACION
}
