package com.example.iobackend.service.web;

import com.example.iobackend.dto.ItemScrapingResult;

import java.util.List;

public interface ItemWebScrapingService {
    List<ItemScrapingResult> findItemsByName(String name);
    List<ItemScrapingResult> findItemsByNames(List<String> names);
}
