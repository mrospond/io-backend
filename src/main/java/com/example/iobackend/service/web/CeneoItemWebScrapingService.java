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

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public List<ItemScrapingResult> findItems(ItemInquiryDto query)  {
        List<ItemScrapingResult> results = new ArrayList<>();
        String mappedUrl = mapNameToUrlQuery(query.getQuery());
        Document document = jsoupConnector.getDocument(mappedUrl);
        Elements shoppingItems = document.select("div.cat-prod-row__body ");

        // Parse all items on the front page
        for (Element item: shoppingItems){
            String imageUrl = getImageUrl(item);
            String ceneoProductUrl = getCeneoProductUrl(item);

            Document productPage = jsoupConnector.getDocument(ceneoProductUrl);

            String productName = getProductName(document);
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
        return urlRoot + "/szukaj-" + name.replace(" ", delimiter);
    }

    private String getImageUrl(Element product) {
        Element image = product.selectFirst("div.cat-prod-row__foto>a>img");
        return "https:" + image.attr("src");
    }

    private String getCeneoProductUrl(Element product) {
        String ceneoProductUrl = product.select("strong.cat-prod-row__name a.go-to-product")
                .attr("href");
        return urlRoot + ceneoProductUrl.replace("#", "");
    }

    private String getProductName(Document productPage) {
        return productPage.select("div.product-top__title h1").text();
    }

    private Element getProduct(Document productPage) {
        return productPage.select("section.product-offers--standard li.product-offers__list__item")
                .first();
    }

    private String getProductPrice(Element product) {
        String valuePrice = product.select("span.value").first().text();
        String pennyPrice = product.select("span.penny").first().text();
        String price = valuePrice + pennyPrice;
        return price.replace(",", ".").replace("\s", "");
    }

    private String getProductShopName(Element product) {
        return product.select("div.js_full-product-offer div.product-offer__container")
                .attr("data-shopurl");
    }

    private String getProductDirectUrl(Element product) {
        return urlRoot + product.select("a.go-to-shop").attr("href");
    }

    private List<String> getProductReviews(Document productPage) {
        return productPage
                .select("div.js_product-review>div.user-post__body>div.user-post__content>div.user-post__text")
                .stream()
                .map(Element::text)
                .toList();
    }

    private String getProductDescription(Document productPage) {
        return productPage.select("div.product-full-description").text();
    }
}
