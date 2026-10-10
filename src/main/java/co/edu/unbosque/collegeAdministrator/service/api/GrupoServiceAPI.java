package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

public interface GrupoServiceAPI extends GenericServiceAPI<Grupo, Integer> {

    /**
     * @param cupoMaximo opcional: null para usar el máximo permitido por los parámetros (tamaño + tolerancia).
     */
    Grupo crearGrupo(Integer idPeriodo, Integer idMateria, String codigo, Integer cupoMaximo);

    List<Grupo> listarPorPeriodo(Integer idPeriodo);

    List<Grupo> listarOferta(Integer idPeriodo, Integer idMateria);

    List<Grupo> listarPorDocente(Integer idDocente, Integer idPeriodo);

    long calcularCuposDisponibles(Integer idGrupo);

    Grupo asignarDocente(Integer idGrupo, Integer idDocente);

    /**
     * @param idAula         opcional: null si el aula se asigna después.
     * @param usoExcepcional obligatorio en true para usar un auditorio (ESP-F-05).
     */
    Grupo agregarSesion(Integer idGrupo, FranjaHoraria franja, Integer idAula, boolean usoExcepcional);

    /**
     * HU-25: mueve una sesión a otra franja o aula. Si la nueva posición no es válida no cambia nada.
     */
    Grupo moverSesion(Integer idGrupo, Integer idSesion, FranjaHoraria franja, Integer idAula, boolean usoExcepcional);

    Grupo quitarSesion(Integer idGrupo, Integer idSesion);

    /**
     * HU-23: advertencias (no bloquean) si con la nueva franja las sesiones quedan en días consecutivos.
     */
    List<String> advertenciasDiasConsecutivos(Integer idGrupo, FranjaHoraria franjaNueva);

    List<Aula> sugerirAulas(Integer idGrupo);

    Grupo cerrarGrupo(Integer idGrupo);

    List<Grupo> listarGruposEnRiesgo(Integer idPeriodo);

    List<Grupo> cerrarGruposBajoMinimo(Integer idPeriodo);

    /**
     * Para el generador automático: devuelve el motivo por el que la sesión no es válida, o null si lo es.
     * No lanza excepciones de negocio, así un intento fallido no marca la transacción para rollback.
     * El docente del grupo, si tiene, debe tener tipo de vinculación.
     */
    String evaluarSesion(Integer idGrupo, FranjaHoraria franja, Integer idAula);

    /**
     * Para el generador automático: motivo por el que el docente no puede tomar el grupo, o null si puede.
     */
    String evaluarDocente(Integer idGrupo, Integer idDocente);

    /**
     * Para el generador automático: como agregarSesion pero sin guardar versión ni bitácora por cada sesión.
     */
    Grupo agregarSesionGenerada(Integer idGrupo, FranjaHoraria franja, Integer idAula);

    /**
     * Para el generador automático: como asignarDocente pero sin guardar versión ni bitácora.
     */
    Grupo asignarDocenteGenerado(Integer idGrupo, Integer idDocente);
}
