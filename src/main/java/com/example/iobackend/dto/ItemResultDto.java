package com.example.iobackend.dto;

import com.example.iobackend.service.domain.annotations.Date;
import com.example.iobackend.service.domain.annotations.Header;
import com.example.iobackend.service.domain.annotations.Url;
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
    @Url
    private String url;
    @Header("Cena")
    private BigDecimal price;
    @Header("Data")
    @Date
    private LocalDateTime timestamp;
}
