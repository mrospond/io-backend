package com.example.iobackend.dto;

import com.example.iobackend.service.domain.Header;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemResultDto {
    @Header("Nazwa")
    private String name;
    @Header("Link")
    private String url;
    @Header("Cena")
    private BigDecimal price;
    @Header("Data")
    private LocalDateTime timestamp;
}
