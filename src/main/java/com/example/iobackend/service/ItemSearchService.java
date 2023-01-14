package com.example.iobackend.service;

import com.example.iobackend.database.entities.ItemResultModel;
import com.example.iobackend.database.entities.UserModel;
import com.example.iobackend.database.repository.UserRepository;
import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.dto.ItemResultDto;
import com.example.iobackend.dto.ItemScrapingResult;
import com.example.iobackend.exceptions.ExportFileException;
import com.example.iobackend.mappers.ItemMapper;
import com.example.iobackend.service.domain.SearchHistoryExporter;
import com.example.iobackend.service.web.ItemWebScrapingService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class ItemSearchService {
    private final List<ItemWebScrapingService> webScrapingServices;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final List<SearchHistoryExporter> searchHistoryExporters;

    public List<ItemScrapingResult> findItems(ItemInquiryDto query, Authentication authentication) {
        List<ItemScrapingResult> result = new ArrayList<>();
        for (ItemWebScrapingService webScrapingService : webScrapingServices) {
            result.addAll(webScrapingService.findItems(query));
        }
        addItemsToHistory(result, authentication);
        return result;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public List<ItemResultDto> getSearchHistory(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return Collections.emptyList();
        }
        String username = ((User) authentication.getPrincipal()).getUsername();
        UserModel userModel = userRepository.findFirstByUsername(username).orElseThrow();
        return userModel.getSearchHistory()
                .stream()
                .map(itemMapper::modelToDto)
                .sorted(Comparator.comparing(ItemResultDto::getTimestamp).reversed())
                .toList();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
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

    @Transactional
    public void exportHistory(String extension, OutputStream output, Authentication authentication) {
        SearchHistoryExporter exporter = getExporter(extension);
        if (exporter != null) {
            List<ItemResultDto> searchHistory = this.getSearchHistory(authentication);
            try {
                exporter.export(searchHistory, output);
            } catch (IOException e) {
                throw new ExportFileException("Unable to export file");
            }
        } else {
            throw new ExportFileException("Unknown file extension");
        }
    }

    public SearchHistoryExporter getExporter(String fileExtension) {
        SearchHistoryExporter exporter = null;
        for (SearchHistoryExporter searchHistoryExporter : searchHistoryExporters) {
            if (searchHistoryExporter.getFileType().getExtension().equalsIgnoreCase(fileExtension)) {
                exporter = searchHistoryExporter;
            }
        }
        if (exporter == null) {
            throw new ExportFileException("Unknown file extension");
        }
        return exporter;
    }
}
