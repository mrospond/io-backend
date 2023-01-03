package com.example.iobackend.controllers;

import com.example.iobackend.service.ItemSearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/search")
public class ItemSearchController {

    private final ItemSearchService itemSearchService;

    @GetMapping
    public ResponseEntity<Object> getItemInquiryResults(String query) {
        return ResponseEntity.ok(itemSearchService.findItems(query));
    }
}
