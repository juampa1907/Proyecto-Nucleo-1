package co.edu.unbosque.collegeAdministrator.utils.exception;

import co.edu.unbosque.collegeAdministrator.dto.response.RespuestaError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger LOG = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<RespuestaError> noEncontrado(RecursoNoEncontradoException e, HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, e.getMessage(), request, null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RespuestaError> rutaNoEncontrada(NoResourceFoundException e, HttpServletRequest request) {
        return respuesta(HttpStatus.NOT_FOUND, "La ruta solicitada no existe", request, null);
    }

    @ExceptionHandler({ReglaNegocioException.class, IllegalArgumentException.class})
    public ResponseEntity<RespuestaError> reglaNegocio(RuntimeException e, HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, e.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaError> validacion(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return respuesta(HttpStatus.BAD_REQUEST, "Hay datos inválidos en la solicitud", request, errores);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespuestaError> cuerpoIlegible(HttpMessageNotReadableException e, HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no tiene un formato válido", request, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RespuestaError> tipoInvalido(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, "El valor '" + e.getValue() + "' no es válido para " + e.getName(), request, null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<RespuestaError> parametroFaltante(MissingServletRequestParameterException e, HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, "Falta el parámetro " + e.getParameterName(), request, null);
    }

    @ExceptionHandler(AutenticacionException.class)
    public ResponseEntity<RespuestaError> autenticacion(AutenticacionException e, HttpServletRequest request) {
        return respuesta(HttpStatus.UNAUTHORIZED, e.getMessage(), request, null);
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<RespuestaError> accesoDenegado(AccesoDenegadoException e, HttpServletRequest request) {
        return respuesta(HttpStatus.FORBIDDEN, e.getMessage(), request, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RespuestaError> sinRol(AccessDeniedException e, HttpServletRequest request) {
        return respuesta(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta acción", request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RespuestaError> integridad(DataIntegrityViolationException e, HttpServletRequest request) {
        LOG.warn("Violación de integridad en {}: {}", request.getRequestURI(), e.getMostSpecificCause().getMessage());
        return respuesta(HttpStatus.CONFLICT,
                "La operación entra en conflicto con datos existentes (registro duplicado o en uso)", request, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<RespuestaError> metodoNoSoportado(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return respuesta(HttpStatus.METHOD_NOT_ALLOWED, "Método " + e.getMethod() + " no permitido en esta ruta", request, null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<RespuestaError> tipoContenido(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        return respuesta(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "El cuerpo debe enviarse como application/json", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError> inesperado(Exception e, HttpServletRequest request) {
        LOG.error("Error inesperado en {}", request.getRequestURI(), e);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado", request, null);
    }

    private ResponseEntity<RespuestaError> respuesta(HttpStatus estado, String mensaje, HttpServletRequest request,
                                                     Map<String, String> erroresCampos) {
        RespuestaError cuerpo = new RespuestaError(LocalDateTime.now(), estado.value(), estado.getReasonPhrase(),
                mensaje, request.getRequestURI(), erroresCampos);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
