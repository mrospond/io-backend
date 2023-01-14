package com.example.iobackend.service.domain;

import com.example.iobackend.dto.ItemResultDto;
import com.example.iobackend.exceptions.ExportFileException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

@Component
public class PdfSearchHistoryExporter implements SearchHistoryExporter {
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

    private void createTable(List<ItemResultDto> items, Document document) throws IllegalAccessException {
        Headers fieldNamesToHeaderNames = SearchHistoryExporter.getHeaderValues(ItemResultDto.class);

        String[] pdfHeader = fieldNamesToHeaderNames.getHeaderNames();
        String[] mappedFields = fieldNamesToHeaderNames.getFieldNames();

        Table table = new Table(pdfHeader.length);
        addHeaderRow(table, pdfHeader);
        for (ItemResultDto item : items) {
            addItemRow(table, item, mappedFields);
        }

        document.add(table);
    }

    private void addHeaderRow(Table table, String[] values) {
        for (String headerValue : values) {
            table.addCell(new Cell().add(new Paragraph(headerValue)));
        }
    }

    private void addItemRow(Table table, ItemResultDto item, String[] fieldsToAdd) throws IllegalAccessException {
        Class<?> clazz = item.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int index = getIndexOf(field.getName(), fieldsToAdd);
            if (index != -1) {
                field.setAccessible(true);
                String value = String.valueOf(field.get(item));
                table.addCell(new Cell().add(new Paragraph(value)));
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
}
