package com.example.iobackend.service;

import com.example.iobackend.database.entities.ItemResultModel;
import com.example.iobackend.database.entities.UserModel;
import com.example.iobackend.database.repository.UserRepository;
import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.dto.ItemResultDto;
import com.example.iobackend.dto.ItemScrapingResult;
import com.example.iobackend.mappers.ItemMapper;
import com.example.iobackend.service.web.ItemWebScrapingService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class ItemSearchService {
    private final List<ItemWebScrapingService> webScrapingServices;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    public List<ItemScrapingResult> findItems(ItemInquiryDto query, Authentication authentication) {
        List<ItemScrapingResult> result = new ArrayList<>();
        for (ItemWebScrapingService webScrapingService : webScrapingServices) {
            result.addAll(webScrapingService.findItems(query));
        }
        addItemsToHistory(result, authentication);
        return result;
    }

    @Transactional(readOnly = true)
    public List<ItemResultDto> getSearchHistory(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return Collections.emptyList();
        }
        String username = ((User) authentication.getPrincipal()).getUsername();
        UserModel userModel = userRepository.findFirstByUsername(username).orElseThrow();
        return userModel.getSearchHistory()
                .stream()
                .map(itemMapper::modelToDto)
                .toList();
    }

    @Transactional
    protected void addItemsToHistory(List<ItemScrapingResult> items, Authentication authentication) {
        if (!(authentication == null || authentication instanceof AnonymousAuthenticationToken)) {
            String username = ((User) authentication.getPrincipal()).getUsername();
            UserModel userModel = userRepository.findFirstByUsername(username).orElseThrow();
            for (ItemScrapingResult item : items) {
                ItemResultModel itemModel = itemMapper.dtoToModel(item);
                userModel.getSearchHistory().add(itemModel);
            }
            userRepository.save(userModel);
        }
    }
}
