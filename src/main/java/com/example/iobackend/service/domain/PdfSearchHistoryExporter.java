package com.example.iobackend.service.domain;

import com.example.iobackend.dto.ItemResultDto;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

public class PdfSearchHistoryExporter implements SearchHistoryExporter<OutputStream> {
    @Override
    public FileType getFileType() {
        return FileType.PDF;
    }

    @Override
    public void export(List<ItemResultDto> searchHistory, OutputStream output) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(output));
        Document document = new Document(pdfDocument);


        document.close();
    }

    private void createTable(List<ItemResultDto> items, Document document) {
        Headers fieldNamesToHeaderNames = SearchHistoryExporter.getHeaderValues(ItemResultDto.class);

        String[] pdfHeader = fieldNamesToHeaderNames.getValuesInOrder();
        String[] mappedFields = fieldNamesToHeaderNames.getKeysInOrder();

        Table table = new Table(pdfHeader.length);
        addHeaderRow(table, pdfHeader);
        for (ItemResultDto item : items) {

        }
    }

    private void addHeaderRow(Table table, String[] values) {
        for (String headerValue : values) {
            table.addCell(new Cell().add(new Paragraph(headerValue)));
        }
    }

    private void addItemRow(Table table, ItemResultDto item, String[] fieldsToAdd) {
        Class<?> clazz = item.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (containsValue(fieldsToAdd, field.getName())) {
                field.setAccessible(true);
                //String.valueOf(field.get(item));
            }
        }
    }

    private boolean containsValue(String[] array, String value) {
        for (String str : array) {
            if (value.equals(str)) {
                return true;
            }
        }
        return false;
    }
}
