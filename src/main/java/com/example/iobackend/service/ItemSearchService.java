package com.example.iobackend.service;

import com.example.iobackend.database.entities.ItemModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemSearchService {
    public List<ItemModel> findItems(String query) {
        return null;
    }
}
