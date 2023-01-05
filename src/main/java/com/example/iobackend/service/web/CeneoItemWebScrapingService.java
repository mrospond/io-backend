package com.example.iobackend.service.web;

import com.example.iobackend.dto.ItemScrapingResult;
import com.example.iobackend.jsoup.JsoupConnector;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@PropertySource("classpath:ceneo.properties")
public class CeneoItemWebScrapingService implements ItemWebScrapingService {
    private final JsoupConnector jsoupConnector;

    @Value("${search.url}")
    private String urlRoot;
    @Value("${query.delimiter}")
    private String delimiter;
    @Value("${div.prod.row.class}")
    private String rowClass;
    @Value("${div.prod.row.foto.class}")
    private String fotoClass;
    @Value("${div.prod.row.content.class}")
    private String contentClass;

    @Override
    public List<ItemScrapingResult> findItemsByName(String name) {
        Document document = jsoupConnector.getDocument(mapNameToUrlQuery(name));
        return null;
    }

    @Override
    public List<ItemScrapingResult> findItemsByNames(List<String> names) {
        return null;
    }

    private String mapNameToUrlQuery(String name) {
        return urlRoot + name.replace(" ", delimiter);
    }
}
