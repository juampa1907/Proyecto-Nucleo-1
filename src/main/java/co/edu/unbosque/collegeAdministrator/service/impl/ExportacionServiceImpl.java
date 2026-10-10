package co.edu.unbosque.collegeAdministrator.service.impl;

import co.edu.unbosque.collegeAdministrator.dto.reporte.TablaReporte;
import co.edu.unbosque.collegeAdministrator.service.api.ExportacionServiceAPI;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportacionServiceImpl implements ExportacionServiceAPI {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Color GRIS_ENCABEZADO = new Color(220, 220, 220);

    @Override
    public byte[] exportarPdf(TablaReporte tabla) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        PdfWriter.getInstance(documento, salida);
        documento.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font fuenteCelda = FontFactory.getFont(FontFactory.HELVETICA, 9);

        documento.add(new Paragraph(tabla.titulo(), fuenteTitulo));
        documento.add(new Paragraph("Generado el " + LocalDateTime.now().format(FORMATO_FECHA), fuenteCelda));
        documento.add(new Paragraph(" "));

        PdfPTable pdfTabla = new PdfPTable(tabla.columnas().size());
        pdfTabla.setWidthPercentage(100);
        pdfTabla.setHeaderRows(1);
        for (String columna : tabla.columnas()) {
            PdfPCell celda = new PdfPCell(new Phrase(columna, fuenteEncabezado));
            celda.setBackgroundColor(GRIS_ENCABEZADO);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTabla.addCell(celda);
        }
        for (List<String> fila : tabla.filas()) {
            for (String valor : fila) {
                pdfTabla.addCell(new Phrase(valor == null ? "" : valor, fuenteCelda));
            }
        }
        documento.add(pdfTabla);
        documento.close();
        return salida.toByteArray();
    }

    @Override
    public byte[] exportarExcel(TablaReporte tabla) {
        try (XSSFWorkbook libro = new XSSFWorkbook(); ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
            Sheet hoja = libro.createSheet(WorkbookUtil.createSafeSheetName(tabla.titulo()));

            org.apache.poi.ss.usermodel.Font negrita = libro.createFont();
            negrita.setBold(true);
            CellStyle estiloEncabezado = libro.createCellStyle();
            estiloEncabezado.setFont(negrita);

            Row filaTitulo = hoja.createRow(0);
            filaTitulo.createCell(0).setCellValue(tabla.titulo());
            filaTitulo.getCell(0).setCellStyle(estiloEncabezado);
            hoja.createRow(1).createCell(0).setCellValue("Generado el " + LocalDateTime.now().format(FORMATO_FECHA));

            Row encabezado = hoja.createRow(3);
            for (int i = 0; i < tabla.columnas().size(); i++) {
                encabezado.createCell(i).setCellValue(tabla.columnas().get(i));
                encabezado.getCell(i).setCellStyle(estiloEncabezado);
            }
            int numeroFila = 4;
            for (List<String> fila : tabla.filas()) {
                Row filaExcel = hoja.createRow(numeroFila++);
                for (int i = 0; i < fila.size(); i++) {
                    filaExcel.createCell(i).setCellValue(fila.get(i) == null ? "" : fila.get(i));
                }
            }
            for (int i = 0; i < tabla.columnas().size(); i++) {
                hoja.autoSizeColumn(i);
            }
            libro.write(salida);
            return salida.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("No fue posible generar el archivo Excel", e);
        }
    }
}
