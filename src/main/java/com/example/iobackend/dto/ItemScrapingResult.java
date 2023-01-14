package com.example.iobackend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ItemScrapingResult {
    private String name;
    //private String url;
    private String price;
    //private BigDecimal shippingPrice;
    //private Long shippingDays;
    private String currency="PLN";
    private String shopName;

    private String ceneoProductUrl;
    private String directShopUrl;

    private List<String> reviews;

    private String productInfo;
    private String imageUrl;
}
