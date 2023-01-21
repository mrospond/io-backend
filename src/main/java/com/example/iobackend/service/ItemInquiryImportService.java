package com.example.iobackend.service;

import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.exceptions.ItemInquiryImportException;
import com.example.iobackend.service.domain.input.ItemInquiryImporter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class ItemInquiryImportService {
    private final ItemInquiryImporter itemInquiryImporter;

    public List<ItemInquiryDto> getItemInquiriesFromFile(MultipartFile file) {
        try {
            return itemInquiryImporter.getItemInquiries(file);
        } catch (IOException e) {
            throw new ItemInquiryImportException(e);
        }
    }
}
