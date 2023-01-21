package com.example.iobackend.service.domain.util;

import com.example.iobackend.service.domain.annotations.Header;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtil {
    public static Headers getHeaderValues(Class<?> clazz) {
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
