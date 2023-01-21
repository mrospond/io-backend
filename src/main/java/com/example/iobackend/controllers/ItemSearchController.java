package com.example.iobackend.controllers;

import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.dto.ItemResultDto;
import com.example.iobackend.dto.ItemScrapingResult;
import com.example.iobackend.service.ItemInquiryImportService;
import com.example.iobackend.service.ItemSearchService;
import com.example.iobackend.service.domain.util.FileType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/search")
public class ItemSearchController {
    private final ItemSearchService itemSearchService;
    private final ItemInquiryImportService itemInquiryImportService;

    @PostMapping
    public ResponseEntity<List<ItemScrapingResult>> getItemInquiryResults(@RequestBody ItemInquiryDto inquiry,
                                                                          Authentication authentication) {
        return ResponseEntity.ok(itemSearchService.findItems(inquiry, authentication));
    }

    @PostMapping("/file")
    public ResponseEntity<List<ItemScrapingResult>> getItemInquiryFileResults(MultipartFile file,
                                                                         Authentication authentication) {
        List<ItemInquiryDto> queries = itemInquiryImportService.getItemInquiriesFromFile(file);
        return ResponseEntity.ok(itemSearchService.findItems(queries, authentication));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ItemResultDto>> getSearchHistory(Authentication authentication) {
        return ResponseEntity.ok(itemSearchService.getSearchHistory(authentication));
    }

    @GetMapping("/history/export")
    public void downloadHistory(@RequestParam(name = "extension") String fileExtension,
                                                    Authentication authentication, HttpServletResponse response) throws IOException {
        FileType fileType = FileType.fromString(fileExtension);

        response.setContentType(fileType.getContentType());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        String dateTime = LocalDateTime.now().format(dateFormatter);
        String headerValue = "attachment; filename=history_" + dateTime + "." + fileType.getExtension();
        response.addHeader("Content-Disposition", headerValue);

        itemSearchService.exportHistory(fileExtension, response.getOutputStream(), authentication);
    }
}
