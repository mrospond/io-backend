package com.example.iobackend.service.web;

import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.dto.ItemScrapingResult;

import java.util.List;

public interface ItemWebScrapingService {
    List<ItemScrapingResult> findItems(ItemInquiryDto query);
}
