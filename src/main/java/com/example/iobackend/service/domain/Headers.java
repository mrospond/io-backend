package com.example.iobackend.service.domain;

import lombok.Data;

@Data
public class Headers {
    private final String[] fieldNames;
    private final String[] headerNames;
}
