package com.example.iobackend.service;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Item {
    private String name;
    private BigDecimal price;
    private String currency;
    private String service;
}
