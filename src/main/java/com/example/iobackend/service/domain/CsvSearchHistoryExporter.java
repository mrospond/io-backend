package com.example.iobackend.service.domain;

import com.example.iobackend.dto.ItemResultDto;
import org.springframework.stereotype.Component;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CsvSearchHistoryExporter implements SearchHistoryExporter<PrintWriter> {
    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }

    @Override
    public void export(List<ItemResultDto> searchHistory, PrintWriter output) throws IOException {
        CsvBeanWriter csvWriter = new CsvBeanWriter(output, CsvPreference.STANDARD_PREFERENCE);
        Headers fieldNamesToHeaderNames = SearchHistoryExporter.getHeaderValues(ItemResultDto.class);

        String[] csvHeader = fieldNamesToHeaderNames.getValuesInOrder();
        String[] nameMapping = fieldNamesToHeaderNames.getKeysInOrder();

        csvWriter.writeHeader(csvHeader);

        for (ItemResultDto item : searchHistory) {
            csvWriter.write(item, nameMapping);
        }
        csvWriter.close();
    }
}
