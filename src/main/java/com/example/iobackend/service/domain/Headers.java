package com.example.iobackend.service.domain;

import lombok.Data;

import java.util.List;

@Data
public class Headers {
    private final List<String[]> headers;

    public String[] getKeysInOrder() {
        return getInOrder(true);
    }

    public String[] getValuesInOrder() {
        return getInOrder(false);
    }

    private String[] getInOrder(boolean getKeys) {
        int index = getKeys ? 0 : 1;
        String[] result = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            result[i] = headers.get(i)[index];
        }
        return result;
    }
}
