package com.example.iobackend.service.domain.export;

import com.example.iobackend.dto.ItemResultDto;
import com.example.iobackend.service.domain.util.FileType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface SearchHistoryExporter {
    FileType getFileType();
    void export(List<ItemResultDto> searchHistory, OutputStream output) throws IOException;
}
