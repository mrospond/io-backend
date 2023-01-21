package com.example.iobackend.dto;

import com.example.iobackend.service.domain.annotations.Header;

// bez lomboka bo super-csv wtedy krzyczy że nie ma konstruktora
public class ItemInquiryDto {
    @Header
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ItemInquiryDto(String query) {
        this.query = query;
    }

    public ItemInquiryDto() {
    }
}
