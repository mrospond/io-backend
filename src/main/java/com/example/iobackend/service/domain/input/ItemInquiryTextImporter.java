package com.example.iobackend.service.domain.input;

import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.service.domain.util.Headers;
import com.example.iobackend.service.domain.util.ReflectionUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemInquiryTextImporter implements ItemInquiryImporter {
    @Override
    public List<ItemInquiryDto> getItemInquiries(MultipartFile file) throws IOException {
        try (
                InputStream inputStream = file.getInputStream();
                Reader reader = new InputStreamReader(inputStream);
                CsvBeanReader csvBeanReader = new CsvBeanReader(reader, CsvPreference.STANDARD_PREFERENCE)
        ) {
            Headers headers = ReflectionUtil.getHeaderValues(ItemInquiryDto.class);
            String[] nameMapping = headers.getFieldNames();
            CellProcessor[] cellProcessors = getProcessors();

            List<ItemInquiryDto> itemInquiries = new ArrayList<>();
            ItemInquiryDto itemInquiry;
            while ((itemInquiry = csvBeanReader.read(ItemInquiryDto.class, nameMapping, cellProcessors)) != null) {
                if (!itemInquiry.getQuery().matches("^[a-zA-Z0-9\\s]*$")) {
                    itemInquiry.setValid(false);
                }
                itemInquiries.add(itemInquiry);
            }
            return itemInquiries;
        }
    }

    private static CellProcessor[] getProcessors() {
        return new CellProcessor[]{
                new NotNull()
        };
    }
}
