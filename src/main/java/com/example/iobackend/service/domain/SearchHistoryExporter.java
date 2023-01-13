package com.example.iobackend.service.domain;

import com.example.iobackend.dto.ItemResultDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public interface SearchHistoryExporter {
    FileType getFileType();
    void export(List<ItemResultDto> searchHistory, PrintWriter writer) throws IOException;

    static List<String[]> getHeaderValues(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String[]> fieldNamesToHeaderNames = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Header.class)) {
                Header annotation = field.getAnnotation(Header.class);
                String key = field.getName();
                String value = "{fieldName}".equals(annotation.value()) ? key : annotation.value();
                fieldNamesToHeaderNames.add(new String[]{key, value});
            }
        }
        return fieldNamesToHeaderNames;
    }
}
