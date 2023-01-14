package com.example.iobackend.service.web;

import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.dto.ItemScrapingResult;
import com.example.iobackend.jsoup.JsoupConnector;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private String mapNameToUrlQuery(String name) {
        return urlRoot + name.replace(" ", delimiter);
    }


//    @Override
//    public List<ItemScrapingResult> findItemsByNames(List<String> names) {
//        return null;
//    }

    private List extractDataFromCeneo(ItemInquiryDto query, String url)  {
        List<ItemScrapingResult> results = new ArrayList<ItemScrapingResult>();

        Document document = jsoupConnector.getDocument(url);
        Elements shoppingItems = document.selectXpath("//div[@class='cat-prod-row__body ']");

        for (Element item: shoppingItems){
            Element image = item.selectXpath("//div[@class='cat-prod-row__foto']//img").first();

            String imageUrl = image.attr("src");
            String productName = image.attr("alt");

            results.add(ItemScrapingResult.builder().imageUrl(imageUrl).name(productName).build());
        }


        return results;

    }
    @Override
    public List<ItemScrapingResult> findItems(ItemInquiryDto query) {
        // TODO: 10.01.2023 To tylko przykładowa lista, trzeba zamienić na prawdziwe wyniki

        List<ItemScrapingResult> itemScrapingResults = extractDataFromCeneo(query, urlRoot);
        return itemScrapingResults;

//        return List.of(
//                ItemScrapingResult.builder()
//                        .name("Syrop na kaszel")
//                        .url("exampleurl.com")
//                        .currency("PLN")
//                        .shippingDays(1L)
//                        .shop("Allegro")
//                        .price(new BigDecimal("15.00"))
//                        .shippingPrice(new BigDecimal("2.00"))
//                        .build(),
//                ItemScrapingResult.builder()
//                        .name("Tabletki do ssania")
//                        .url("exampleurl.com")
//                        .currency("PLN")
//                        .shippingDays(2L)
//                        .shop("Jakiśsklep")
//                        .price(new BigDecimal("10.00"))
//                        .shippingPrice(new BigDecimal("1.50"))
//                        .build()
//        );
    }
}
