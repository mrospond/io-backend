package com.example.iobackend.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupConnector {
    private static final int DEFAULT_TIMEOUT = 3000;

    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .timeout(DEFAULT_TIMEOUT)
                .url(url)
                .get();
    }
}
