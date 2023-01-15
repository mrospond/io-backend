package com.example.iobackend.service.domain.export.util;

import com.example.iobackend.exceptions.ExportFileException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum FileType {
    CSV("csv", "text/csv"),
    PDF("pdf", "application/pdf");

    private final String extension;
    private final String contentType;

    public static FileType fromString(String type) {
        return Arrays.stream(values())
                .filter(fileType -> fileType.extension.equals(type))
                .findFirst()
                .orElseThrow(() -> new ExportFileException("Unknown extension: " + type));
    }
}
