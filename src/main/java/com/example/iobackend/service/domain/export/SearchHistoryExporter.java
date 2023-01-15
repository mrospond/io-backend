package com.example.iobackend.service.domain.export;

import com.example.iobackend.dto.ItemResultDto;
import com.example.iobackend.service.domain.export.annotations.Header;
import com.example.iobackend.service.domain.export.util.FileType;
import com.example.iobackend.service.domain.export.util.Headers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public interface SearchHistoryExporter {
    FileType getFileType();
    void export(List<ItemResultDto> searchHistory, OutputStream output) throws IOException;

    static Headers getHeaderValues(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        List<String> headerNames = new ArrayList<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Header.class)) {
                Header annotation = field.getAnnotation(Header.class);
                String key = field.getName();
                String value = "{fieldName}".equals(annotation.value()) ? key : annotation.value();
                fieldNames.add(key);
                headerNames.add(value);
            }
        }
        return new Headers(fieldNames.toArray(String[]::new), headerNames.toArray(String[]::new));
    }
}
