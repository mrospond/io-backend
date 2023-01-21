package com.example.iobackend.service.web;

import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.dto.ItemScrapingResult;
import com.example.iobackend.jsoup.JsoupConnector;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CeneoItemWebScrapingService implements ItemWebScrapingService {
    private final JsoupConnector jsoupConnector;
    private final CeneoProperties properties;

    @Override
    public List<ItemScrapingResult> findItems(ItemInquiryDto query)  {
        if (!query.isValid()) {
            return Collections.emptyList();
        }
        List<ItemScrapingResult> results = new ArrayList<>();
        String mappedUrl = mapNameToUrlQuery(query.getQuery());
        Document document = jsoupConnector.getDocument(mappedUrl);
        Elements shoppingItems = document.select(properties.getShoppingItemsSelector());

        // Parse all items on the front page
        for (Element item: shoppingItems){
            String imageUrl = getImageUrl(item);
            String ceneoProductUrl = getCeneoProductUrl(item);

            Document productPage = jsoupConnector.getDocument(ceneoProductUrl);

            String productName = getProductName(productPage);
            Element product = getProduct(productPage);
            String price = getProductPrice(product);
            String shopName = getProductShopName(product);
            String directShopUrl = getProductDirectUrl(product);
            List<String> productReviews = getProductReviews(productPage);
            String productDescription = getProductDescription(productPage);

            results.add(
                    ItemScrapingResult.builder()
                            .imageUrl(imageUrl)
                            .name(productName)
                            .ceneoProductUrl(ceneoProductUrl)
                            .directShopUrl(directShopUrl)
                            .price(price)
                            .currency("PLN")
                            .shopName(shopName)
                            .reviews(productReviews)
                            .productInfo(productDescription)
                            .timestamp(LocalDateTime.now())
                            .build()
            );
        }

        return results;
    }

    private String mapNameToUrlQuery(String name) {
        return properties.getUrlRoot() + properties.getKeyword() + name.replace(" ", properties.getDelimiter());
    }

    private String getImageUrl(Element product) {
        Element image = product.selectFirst(properties.getProductImageUrlSelector());
        return CeneoProperties.HTTPS + image.attr(CeneoProperties.SRC);
    }

    private String getCeneoProductUrl(Element product) {
        String ceneoProductUrl = product.select(properties.getProductCeneoUrlSelector())
                .attr(CeneoProperties.HREF);
        return properties.getUrlRoot() + ceneoProductUrl.replace("#", "");
    }

    private String getProductName(Document productPage) {
        return productPage.select(properties.getProductNameSelector()).text();
    }

    private Element getProduct(Document productPage) {
        return productPage.select(properties.getProductSelector()).first();
    }

    private String getProductPrice(Element product) {
        String valuePrice = product.select(properties.getProductPriceValueSelector()).first().text();
        String pennyPrice = product.select(properties.getProductPricePennySelector()).first().text();
        String price = valuePrice + pennyPrice;
        return price.replace(",", ".").replace("\s", "");
    }

    private String getProductShopName(Element product) {
        return product.select(properties.getProductShopNameSelector())
                .attr(properties.getProductShopNameAttributeSelector());
    }

    private String getProductDirectUrl(Element product) {
        return properties.getUrlRoot() +
                product.select(properties.getProductDirectUrlSelector()).attr(CeneoProperties.HREF);
    }

    private List<String> getProductReviews(Document productPage) {
        return productPage
                .select(properties.getProductReviewsSelector())
                .stream()
                .map(Element::text)
                .toList();
    }

    private String getProductDescription(Document productPage) {
        return productPage.select(properties.getProductDescriptionSelector()).text();
    }
}
