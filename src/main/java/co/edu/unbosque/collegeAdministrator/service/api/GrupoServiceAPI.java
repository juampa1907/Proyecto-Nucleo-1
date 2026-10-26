package co.edu.unbosque.collegeAdministrator.service.api;

import co.edu.unbosque.collegeAdministrator.entity.Aula;
import co.edu.unbosque.collegeAdministrator.entity.FranjaHoraria;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.utils.GenericServiceAPI;

import java.util.List;

public interface GrupoServiceAPI extends GenericServiceAPI<Grupo, Integer> {


    Grupo crearGrupo(Integer idPeriodo, Integer idMateria, String codigo, Integer cupoMaximo);

    List<Grupo> listarPorPeriodo(Integer idPeriodo);

    List<Grupo> listarOferta(Integer idPeriodo, Integer idMateria);

    List<Grupo> listarPorDocente(Integer idDocente, Integer idPeriodo);

    long calcularCuposDisponibles(Integer idGrupo);

    Grupo asignarDocente(Integer idGrupo, Integer idDocente);


    Grupo agregarSesion(Integer idGrupo, FranjaHoraria franja, Integer idAula, boolean usoExcepcional);


    Grupo moverSesion(Integer idGrupo, Integer idSesion, FranjaHoraria franja, Integer idAula, boolean usoExcepcional);

    Grupo quitarSesion(Integer idGrupo, Integer idSesion);


    List<String> advertenciasDiasConsecutivos(Integer idGrupo, FranjaHoraria franjaNueva);

    List<Aula> sugerirAulas(Integer idGrupo);

    Grupo cerrarGrupo(Integer idGrupo);

    List<Grupo> listarGruposEnRiesgo(Integer idPeriodo);

    List<Grupo> cerrarGruposBajoMinimo(Integer idPeriodo);


    String evaluarSesion(Integer idGrupo, FranjaHoraria franja, Integer idAula);

    String evaluarDocente(Integer idGrupo, Integer idDocente);


    Grupo agregarSesionGenerada(Integer idGrupo, FranjaHoraria franja, Integer idAula);


    Grupo asignarDocenteGenerado(Integer idGrupo, Integer idDocente);
}
