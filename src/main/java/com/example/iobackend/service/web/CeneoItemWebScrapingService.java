package com.example.iobackend.service.web;

import com.example.iobackend.dto.ItemInquiryDto;
import com.example.iobackend.dto.ItemScrapingResult;
import com.example.iobackend.jsoup.JsoupConnector;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return urlRoot + "szukaj-" + name.replace(" ", delimiter);
    }


//    @Override
//    public List<ItemScrapingResult> findItemsByNames(List<String> names) {
//        return null;
//    }

    private String constructUrlFromParameters(){return new String();}

    private String findRedirectedUrl(String url) throws IOException {

//        Connection.Response response = Jsoup.connect(url).followRedirects(true).execute();
////
////        System.out.println(response.statusCode() + " : " + url);
////
////        if (response.hasHeader("location")) {
////            String redirectUrl = response.header("location");
////            findRedirectedUrl(redirectUrl);
////        }
        return url;

    }

    private List extractDataFromCeneo(ItemInquiryDto query, String urlRoot)  {
        List<ItemScrapingResult> results = new ArrayList<ItemScrapingResult>();
        String mappedUrl = mapNameToUrlQuery(query.getQuery());
        Document document = jsoupConnector.getDocument(mappedUrl);
        Elements shoppingItems = document.select("div.cat-prod-row__body ");

        // Parse all items on the front page
        for (Element item: shoppingItems){
            Element image = item.selectFirst("div.cat-prod-row__foto>a>img");

            String imageUrl = "https:" + image.attr("src");

            String ceneoProductUrl = item.select("strong.cat-prod-row__name a.go-to-product").attr("href");
            ceneoProductUrl = "https://www.ceneo.pl" + ceneoProductUrl;

            try{
                Document productPageDocument = Jsoup.connect(ceneoProductUrl).timeout(10000).get();
                String productName = productPageDocument.select("div.product-top__title h1").text();
                Element product = productPageDocument.select("section.product-offers--standard li.product-offers__list__item").first();
                String valuePrice = product.select("span.value").first().text();
                String pennyPrice = product.select("span.penny").first().text();
                String price = valuePrice + pennyPrice;
                String shopName = product.select("div.js_full-product-offer div.product-offer__container").attr("data-shopurl");

                String shopRelativeUrl = product.select("a.go-to-shop").attr("href");
                String shopSpecificUrl = "https://www.ceneo.pl" + shopRelativeUrl;


                List<String> productReviews = productPageDocument.select("div.js_product-review>div.user-post__body>div.user-post__content>div.user-post__text").stream().map(el -> el.text()).collect(Collectors.toList());
                String productDescription = productPageDocument.select("div.product-full-description").text();
                results.add(
                        ItemScrapingResult.builder()
                                .imageUrl(imageUrl)
                                .name(productName)
                                .ceneoProductUrl(ceneoProductUrl)
                                .directShopUrl(shopSpecificUrl)
                                .price(price.replace(",", "."))
                                .currency("PLN")
                                .shopName(shopName)
                                .reviews((productReviews))
                                .productInfo(productDescription)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return results;
    }
    @Override
    public List<ItemScrapingResult> findItems(ItemInquiryDto query) {
        // TODO: 10.01.2023 To tylko przykładowa lista, trzeba zamienić na prawdziwe wyniki
        //String queryUrl = constructUrlFromParameters(query);
        List<ItemScrapingResult> itemScrapingResults = extractDataFromCeneo(query, urlRoot);
        return itemScrapingResults;
    }
}
