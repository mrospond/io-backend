package com.example.iobackend.jsoup;

import com.example.iobackend.exceptions.JsoupConnectionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupConnector {
    private static final int DEFAULT_TIMEOUT = 10000;

    public Document getDocument(String url) {
        try {
            return Jsoup.connect(url)
                    .timeout(DEFAULT_TIMEOUT)
                    .url(url)
                    .get();
        } catch (IOException e) {
            throw new JsoupConnectionException(e);
        }
    }
}
