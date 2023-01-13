package com.example.iobackend.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileType {
    CSV("csv", "text/csv"),
    PDF("pdf", "application/pdf");

    private final String extension;
    private final String contentType;
}
