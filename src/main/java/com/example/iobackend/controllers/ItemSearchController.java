package com.example.iobackend.controllers;

import com.example.iobackend.database.entities.ItemModel;
import com.example.iobackend.service.ItemSearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/search")
public class ItemSearchController {
    private final ItemSearchService itemSearchService;

    @PostMapping
    public ResponseEntity<List<ItemModel>> getItemInquiryResults(@RequestBody String query, Principal user) {
        return ResponseEntity.ok(itemSearchService.findItems(query));
    }

    @GetMapping
    public ResponseEntity<?> getSomething() {
        return ResponseEntity.ok().build();
    }
}
