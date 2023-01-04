package com.example.iobackend.service.web;

import com.example.iobackend.dto.ItemScrapingResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CeneoItemWebScrapingService implements ItemWebScrapingService {
    private static final String URL_ROOT = "https://www.ceneo.pl/Zdrowie";
    private static final String URL_QUERY = ";szukaj-";

    @Override
    public List<ItemScrapingResult> findItemsByName(String name) {
        return null;
    }

    @Override
    public List<ItemScrapingResult> findItemsByNames(List<String> names) {
        return null;
    }
}
