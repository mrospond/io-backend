package com.example.iobackend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ItemScrapingResult {
    private String name;
    private String url;
    private BigDecimal price;
    private BigDecimal shippingPrice;
    private Long shippingDays;
    private String currency;
    private String shop;

    private String imageUrl;
}
