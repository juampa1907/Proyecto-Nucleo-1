package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.entity.DetalleHorario;
import co.edu.unbosque.collegeAdministrator.entity.Estudiante;
import co.edu.unbosque.collegeAdministrator.entity.Grupo;
import co.edu.unbosque.collegeAdministrator.entity.InscripcionMateria;
import co.edu.unbosque.collegeAdministrator.entity.PeriodoAcademico;
import co.edu.unbosque.collegeAdministrator.repository.MatriculaPeriodoRepository;
import co.edu.unbosque.collegeAdministrator.service.api.ComprobanteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.EstudianteServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.InscripcionMateriaServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.PeriodoAcademicoServiceAPI;
import co.edu.unbosque.collegeAdministrator.utils.exception.ReglaNegocioException;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Implementación de la generación de comprobantes de carga académica.
 */
@Service
@Transactional
public class ComprobanteServiceImpl implements ComprobanteServiceAPI {

    /** Formato de la fecha de generación del comprobante. */
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /** Encabezados de la tabla del comprobante. */
    private static final List<String> COLUMNAS = List.of("Código", "Materia", "Grupo", "Créditos", "Docente", "Horario", "Estado");

    /** El servicio de gestión de estudiantes. */
    private final EstudianteServiceAPI estudianteService;
    /** El servicio de gestión de periodos académicos. */
    private final PeriodoAcademicoServiceAPI periodoAcademicoService;
    /** El servicio de inscripción de materias. */
    private final InscripcionMateriaServiceAPI inscripcionMateriaService;
    /** El repositorio de matrículas por periodo. */
    private final MatriculaPeriodoRepository matriculaPeriodoRepository;

    /**
     * Crea una instancia de {@code ComprobanteServiceImpl} con las dependencias requeridas.
     *
     * @param estudianteService el servicio de gestión de estudiantes
     * @param periodoAcademicoService el servicio de gestión de periodos académicos
     * @param inscripcionMateriaService el servicio de inscripción de materias
     * @param matriculaPeriodoRepository el repositorio de matrículas por periodo
     */
    public ComprobanteServiceImpl(EstudianteServiceAPI estudianteService,
                                  PeriodoAcademicoServiceAPI periodoAcademicoService,
                                  InscripcionMateriaServiceAPI inscripcionMateriaService,
                                  MatriculaPeriodoRepository matriculaPeriodoRepository) {
        this.estudianteService = estudianteService;
        this.periodoAcademicoService = periodoAcademicoService;
        this.inscripcionMateriaService = inscripcionMateriaService;
        this.matriculaPeriodoRepository = matriculaPeriodoRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>El código de verificación es el SHA-256 del contenido del comprobante.
     */
    @Override
    public byte[] generarComprobante(Integer idEstudiante, Integer idPeriodo) {
        Estudiante estudiante = estudianteService.buscarPorId(idEstudiante);
        PeriodoAcademico periodo = periodoAcademicoService.buscarPorId(idPeriodo);
        List<InscripcionMateria> carga = inscripcionMateriaService.listarCargaDelPeriodo(idEstudiante, idPeriodo);
        if (carga.isEmpty()) {
            throw new ReglaNegocioException("No tienes materias inscritas en el periodo " + periodo.getCodigo());
        }

        String fecha = LocalDateTime.now().format(FORMATO_FECHA);
        List<List<String>> filas = new ArrayList<>();
        int totalCreditos = 0;
        for (InscripcionMateria inscripcion : carga) {
            Grupo grupo = inscripcion.getGrupo();
            totalCreditos += grupo.getMateria().getNumeroCreditos();
            filas.add(List.of(
                    grupo.getMateria().getCodigo(),
                    grupo.getMateria().getNombre(),
                    grupo.getCodigo(),
                    String.valueOf(grupo.getMateria().getNumeroCreditos()),
                    grupo.getDocente() == null ? "Por asignar" : grupo.getDocente().getUsername(),
                    describirHorario(grupo),
                    inscripcion.getEstado().name()));
        }

        String contenido = estudiante.getUsername() + "|" + periodo.getCodigo() + "|" + fecha + "|"
                + filas.stream().map(fila -> String.join(";", fila)).collect(Collectors.joining("|"));
        String codigoVerificacion = calcularSha256(contenido);
        matriculaPeriodoRepository.findByEstudiante_IdUsuarioAndPeriodo_IdPeriodo(idEstudiante, idPeriodo)
                .ifPresent(matricula -> matricula.setCodigoVerificacion(codigoVerificacion));

        return construirPdf(estudiante, periodo, fecha, filas, totalCreditos, codigoVerificacion);
    }

    /**
     * Construye el documento PDF del comprobante.
     *
     * @param estudiante el estudiante
     * @param periodo el periodo
     * @param fecha la fecha de generación
     * @param filas las filas de la tabla de materias
     * @param totalCreditos el total de créditos
     * @param codigoVerificacion el código de verificación
     * @return el contenido del archivo PDF
     */
    private byte[] construirPdf(Estudiante estudiante, PeriodoAcademico periodo, String fecha, List<List<String>> filas,
                                int totalCreditos, String codigoVerificacion) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        PdfWriter.getInstance(documento, salida);
        documento.open();

        Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font negrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 9);

        documento.add(new Paragraph("Universidad El Bosque — Comprobante de carga académica", titulo));
        documento.add(new Paragraph("Estudiante: " + estudiante.getUsername() + " (" + estudiante.getCorreo() + ")", normal));
        documento.add(new Paragraph("Periodo: " + periodo.getCodigo(), normal));
        documento.add(new Paragraph("Fecha de generación: " + fecha, normal));
        documento.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(COLUMNAS.size());
        tabla.setWidthPercentage(100);
        tabla.setHeaderRows(1);
        for (String columna : COLUMNAS) {
            PdfPCell celda = new PdfPCell(new Phrase(columna, negrita));
            celda.setBackgroundColor(new Color(220, 220, 220));
            tabla.addCell(celda);
        }
        filas.forEach(fila -> fila.forEach(valor -> tabla.addCell(new Phrase(valor, normal))));
        documento.add(tabla);

        documento.add(new Paragraph(" "));
        documento.add(new Paragraph("Total de créditos: " + totalCreditos, negrita));
        documento.add(new Paragraph("Código de verificación (SHA-256): " + codigoVerificacion, normal));
        documento.add(new Paragraph("Documento generado por el Sistema de Gestión de Horarios y Matrícula.", normal));
        documento.close();
        return salida.toByteArray();
    }

    /**
     * Describe las sesiones de un grupo.
     *
     * @param grupo el grupo
     * @return las sesiones separadas por punto y coma
     */
    private String describirHorario(Grupo grupo) {
        return grupo.getSesiones().stream()
                .map(this::describirSesion)
                .collect(Collectors.joining("; "));
    }

    /**
     * Describe una sesión con su franja y su aula.
     *
     * @param sesion la sesión
     * @return la descripción de la sesión
     */
    private String describirSesion(DetalleHorario sesion) {
        return sesion.getFranja() + (sesion.getAula() == null ? "" : " (" + sesion.getAula().getCodigo() + ")");
    }

    /**
     * Calcula el resumen SHA-256 de un texto.
     *
     * @param contenido el texto
     * @return el resumen en hexadecimal
     * @throws IllegalStateException si la JVM no dispone de SHA-256
     */
    private String calcularSha256(String contenido) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(contenido.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no está disponible en la JVM", e);
        }
    }
}
