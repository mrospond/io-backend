package com.example.iobackend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemScrapingResult {
    private String name;
    private String price;
    private String currency;
    private String shopName;
    private String ceneoProductUrl;
    private String directShopUrl;
    private List<String> reviews;
    private String productInfo;
    private String imageUrl;
    private LocalDateTime timestamp;
}
