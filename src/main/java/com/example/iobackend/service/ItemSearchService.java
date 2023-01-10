package com.example.iobackend.service;

import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.dto.ItemScrapingResult;
import com.example.iobackend.service.web.ItemWebScrapingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ItemSearchService {
    private final List<ItemWebScrapingService> webScrapingServices;
    public List<ItemScrapingResult> findItems(ItemInquiryDto query) {
        List<ItemScrapingResult> result = new ArrayList<>();
        for (ItemWebScrapingService webScrapingService : webScrapingServices) {
            result.addAll(webScrapingService.findItems(query));
        }
        return result;
    }
}
