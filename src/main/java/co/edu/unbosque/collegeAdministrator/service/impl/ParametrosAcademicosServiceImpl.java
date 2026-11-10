package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.ParametrosAcademicos;
import co.edu.unbosque.collegeAdministrator.repository.ParametrosAcademicosRepository;
import co.edu.unbosque.collegeAdministrator.service.api.BitacoraServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.ParametrosAcademicosServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

/**
 * Implementación de las operaciones sobre los parámetros académicos.
 */
@Service
@Transactional
public class ParametrosAcademicosServiceImpl implements ParametrosAcademicosServiceAPI {

    /** El repositorio de parámetros académicos. */
    private final ParametrosAcademicosRepository parametrosAcademicosRepository;
    /** El servicio de auditoría donde se registran las acciones. */
    private final BitacoraServiceAPI bitacoraService;

    /**
     * Crea una instancia de {@code ParametrosAcademicosServiceImpl} con las dependencias requeridas.
     *
     * @param parametrosAcademicosRepository el repositorio de parámetros académicos
     * @param bitacoraService el servicio de auditoría donde se registran las acciones
     */
    public ParametrosAcademicosServiceImpl(ParametrosAcademicosRepository parametrosAcademicosRepository,
                                           BitacoraServiceAPI bitacoraService) {
        this.parametrosAcademicosRepository = parametrosAcademicosRepository;
        this.bitacoraService = bitacoraService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParametrosAcademicos obtener() {
        return parametrosAcademicosRepository.findFirstByOrderByIdParametrosAsc()
                .orElseGet(() -> parametrosAcademicosRepository.save(new ParametrosAcademicos()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParametrosAcademicos actualizar(ParametrosAcademicos parametros) {
        validarPositivo(parametros.getTamanoMaximoGrupo(), "El tamaño máximo de grupo");
        validarPositivo(parametros.getDuracionSesionMinutos(), "La duración de la sesión");
        validarPositivo(parametros.getMaxSesionesSemanales(), "El máximo de sesiones semanales");
        validarPositivo(parametros.getDiasAnticipacionAviso(), "Los días de anticipación de los avisos");
        Integer umbral = parametros.getUmbralSubutilizacionPorcentaje();
        if (umbral == null || umbral < 0 || umbral > 100) {
            throw new ReglaNegocioException("El umbral de subutilización debe estar entre 0 y 100");
        }
        if (parametros.getToleranciaCupoPorcentaje() == null || parametros.getToleranciaCupoPorcentaje() < 0) {
            throw new ReglaNegocioException("La tolerancia de cupo no puede ser negativa");
        }
        validarRango(parametros.getHoraInicioSemana(), parametros.getHoraFinSemana(), "La franja de lunes a viernes");
        validarRango(parametros.getHoraInicioSabado(), parametros.getHoraFinSabado(), "La franja del sábado");
        validarRango(parametros.getInicioAlmuerzo(), parametros.getFinAlmuerzo(), "La franja de almuerzo");

        Integer minimo = parametros.getMinimoEstudiantesGrupo();
        if (minimo != null && (minimo <= 0 || minimo > parametros.getTamanoMaximoGrupo())) {
            throw new ReglaNegocioException("El mínimo de estudiantes debe estar entre 1 y el tamaño máximo de grupo");
        }
        Integer creditosMinimos = parametros.getCreditosMinimos();
        Integer creditosMaximos = parametros.getCreditosMaximos();
        if (creditosMinimos != null) {
            validarPositivo(creditosMinimos, "Los créditos mínimos");
        }
        if (creditosMaximos != null) {
            validarPositivo(creditosMaximos, "Los créditos máximos");
        }
        if (creditosMinimos != null && creditosMaximos != null && creditosMinimos > creditosMaximos) {
            throw new ReglaNegocioException("Los créditos mínimos no pueden superar a los máximos");
        }

        parametros.setIdParametros(obtener().getIdParametros());
        bitacoraService.registrar("ACTUALIZAR_PARAMETROS", "Parámetros académicos actualizados");
        return parametrosAcademicosRepository.save(parametros);
    }

    /**
     * Verifica que un valor sea mayor que cero.
     *
     * @param valor el valor
     * @param campo el nombre del campo para el mensaje
     * @throws ReglaNegocioException si no es positivo
     */
    private void validarPositivo(Integer valor, String campo) {
        if (valor == null || valor <= 0) {
            throw new ReglaNegocioException(campo + " debe ser mayor a 0");
        }
    }

    /**
     * Verifica que una hora de inicio sea anterior a la de fin.
     *
     * @param inicio la hora de inicio
     * @param fin la hora de fin
     * @param campo el nombre del campo para el mensaje
     * @throws ReglaNegocioException si el rango no es válido
     */
    private void validarRango(LocalTime inicio, LocalTime fin, String campo) {
        if (inicio == null || fin == null || !inicio.isBefore(fin)) {
            throw new ReglaNegocioException(campo + " debe tener una hora de inicio anterior a la de fin");
        }
    }
}
