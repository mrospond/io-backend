package com.example.iobackend.service.domain.input;

import com.example.iobackend.dto.ItemInquiryDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ItemInquiryImporter {
    List<ItemInquiryDto> getItemInquiries(MultipartFile file) throws IOException;
}
