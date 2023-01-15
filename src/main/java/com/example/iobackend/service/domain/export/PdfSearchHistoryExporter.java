package com.example.iobackend.service.domain.export;

import com.example.iobackend.dto.ItemResultDto;
import com.example.iobackend.exceptions.ExportFileException;
import com.example.iobackend.service.domain.export.annotations.Date;
import com.example.iobackend.service.domain.export.annotations.Url;
import com.example.iobackend.service.domain.export.util.FileType;
import com.example.iobackend.service.domain.export.util.Headers;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfSearchHistoryExporter implements SearchHistoryExporter {
    private static final Map<Character, Character> polishChars = new HashMap<>(Map.ofEntries(
            Map.entry('ą', 'a'),
            Map.entry('ć', 'c'),
            Map.entry('ę', 'e'),
            Map.entry('ł', 'l'),
            Map.entry('ń', 'n'),
            Map.entry('ó', 'o'),
            Map.entry('ś', 's'),
            Map.entry('ź', 'z'),
            Map.entry('ż', 'z')
    ));

    @Override
    public FileType getFileType() {
        return FileType.PDF;
    }

    @Override
    public void export(List<ItemResultDto> searchHistory, OutputStream output) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(output));
        Document document = new Document(pdfDocument);
        try {
            createTable(searchHistory, document);
        } catch (IllegalAccessException e) {
            throw new ExportFileException(e.getMessage());
        }

        document.close();
    }

    private void createTable(List<ItemResultDto> items, Document document) throws IllegalAccessException, IOException {
        Headers fieldNamesToHeaderNames = SearchHistoryExporter.getHeaderValues(ItemResultDto.class);

        String[] pdfHeader = fieldNamesToHeaderNames.getHeaderNames();
        String[] mappedFields = fieldNamesToHeaderNames.getFieldNames();

        PdfFont font = createFont(document.getPdfDocument());
        Table table = new Table(pdfHeader.length);
        addHeaderRow(table, pdfHeader, font);

        for (ItemResultDto item : items) {
            addItemRow(table, item, mappedFields, font);
        }

        document.add(table);
    }

    private void addHeaderRow(Table table, String[] values, PdfFont font) {
        for (String headerValue : values) {
            table.addHeaderCell(new Cell().add(
                    new Paragraph(replacePolishChars(headerValue))
                            .setFont(font))
            );
        }
    }

    private void addItemRow(Table table, ItemResultDto item, String[] fieldsToAdd, PdfFont font) throws IllegalAccessException {
        Class<?> clazz = item.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int index = getIndexOf(field.getName(), fieldsToAdd);
            if (index != -1) {
                field.setAccessible(true);
                Cell cell;
                String value = String.valueOf(field.get(item));
                if (isLink(field)) {
                    String link = "Link";
                    cell = new Cell().add(
                            new Paragraph(link)
                                    .setUnderline()
                                    .setFontColor(ColorConstants.BLUE)
                                    .setFont(font)
                    );
                    cell.setAction(PdfAction.createURI(value));
                } else if (isDate(field)) {
                    LocalDateTime date = LocalDateTime.parse(value);
                    String format = getDateFormat(field);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                    cell = new Cell().add(
                            new Paragraph(date.format(formatter))
                                    .setFont(font)
                    );
                } else {
                    cell = new Cell().add(
                            new Paragraph(replacePolishChars(value))
                                    .setFont(font)
                    );
                }

                table.addCell(cell);
            }
        }
    }

    private int getIndexOf(String value, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (value.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    private boolean isLink(Field field) {
        return field.isAnnotationPresent(Url.class);
    }

    private boolean isDate(Field field) {
        return field.isAnnotationPresent(Date.class);
    }

    private String getDateFormat(Field field) {
        return field.getAnnotation(Date.class).format();
    }

    private PdfFont createFont(PdfDocument document) throws IOException {
        FontProgram fontProgram = FontProgramFactory.createFont(StandardFonts.TIMES_ROMAN);
        return PdfFontFactory.createFont(fontProgram.toString(), PdfEncodings.UTF8, document);
    }

    private String replacePolishChars(String string) {
        char[] letters = string.toCharArray();
        for (int i = 0; i < letters.length; i++) {
            if (polishChars.containsKey(letters[i])) {
                letters[i] = polishChars.get(letters[i]);
            }
        }
        return String.valueOf(letters);
    }
}
