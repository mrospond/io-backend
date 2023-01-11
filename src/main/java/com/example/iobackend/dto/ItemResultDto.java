package com.example.iobackend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemResultDto {
    private String name;
    private String url;
    private BigDecimal price;
    private LocalDateTime timestamp;
}
