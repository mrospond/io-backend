package com.example.iobackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemInquiryDto {
    private String query;
}
