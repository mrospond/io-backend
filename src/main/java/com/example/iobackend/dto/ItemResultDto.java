package com.example.iobackend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemResultDto {
    private String name;
    private String url;
    private String price;
    private LocalDateTime timestamp;
}
