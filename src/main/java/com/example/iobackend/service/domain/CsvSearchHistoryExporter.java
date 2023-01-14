package com.example.iobackend.service.domain;

import com.example.iobackend.dto.ItemResultDto;
import org.springframework.stereotype.Component;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CsvSearchHistoryExporter implements SearchHistoryExporter {
    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }

    @Override
    public void export(List<ItemResultDto> searchHistory, PrintWriter writer) throws IOException {
        CsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
        List<String[]> fieldNamesToHeaderNames = SearchHistoryExporter.getHeaderValues(ItemResultDto.class);

        String[] csvHeader = getInOrder(fieldNamesToHeaderNames, false);
        String[] nameMapping = getInOrder(fieldNamesToHeaderNames, true);

        csvWriter.writeHeader(csvHeader);

        for (ItemResultDto item : searchHistory) {
            csvWriter.write(item, nameMapping);
        }
        csvWriter.close();
    }

    private String[] getInOrder(List<String[]> list, boolean getKeys) {
        int index = getKeys ? 0 : 1;
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i)[index];
        }
        return result;
    }
}
