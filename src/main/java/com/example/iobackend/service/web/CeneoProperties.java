package com.example.iobackend.service.web;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@PropertySource("classpath:ceneo.properties")
@Component
public class CeneoProperties {
    public static final String HTTPS = "https:";
    public static final String SRC = "src";
    public static final String HREF = "href";

    @Value("${search.url}")
    private String urlRoot;
    @Value("${search.queryDelimiter}")
    private String delimiter;
    @Value("${search.keyword}")
    private String keyword;
    @Value("${mainPage.shoppingItems}")
    private String shoppingItemsSelector;
    @Value("${product.imageUrl}")
    private String productImageUrlSelector;
    @Value("${product.ceneoUrl}")
    private String productCeneoUrlSelector;
    @Value("${product.name}")
    private String productNameSelector;
    @Value("${product.product}")
    private String productSelector;
    @Value("${product.price.value}")
    private String productPriceValueSelector;
    @Value("${product.price.penny}")
    private String productPricePennySelector;
    @Value("${product.shopName}")
    private String productShopNameSelector;
    @Value("${product.shopName.attr}")
    private String productShopNameAttributeSelector;
    @Value("${product.directUrl}")
    private String productDirectUrlSelector;
    @Value("${product.reviews}")
    private String productReviewsSelector;
    @Value("${product.description}")
    private String productDescriptionSelector;

}
