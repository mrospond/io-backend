package com.example.iobackend.dto;

import com.example.iobackend.service.domain.annotations.Header;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

// bez lomboka bo super-csv wtedy krzyczy że nie ma konstruktora
public class ItemInquiryDto {
    @Header
    @NotBlank(message = "Query cannot be blank")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Query can only contain alphanumeric characters")
    private String query;

    @JsonIgnore
    private boolean valid = true;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public ItemInquiryDto(String query) {
        this.query = query;
    }

    public ItemInquiryDto() {
    }
}
