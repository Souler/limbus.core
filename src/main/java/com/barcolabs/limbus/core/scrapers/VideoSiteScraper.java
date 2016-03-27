package com.barcolabs.limbus.core.scrapers;

import com.barcolabs.limbus.core.exceptions.ScrapingException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class VideoSiteScraper extends Scraper {
    public abstract String get(String uri) throws IOException, ScrapingException;
    public abstract String getHandledHost();
    public boolean canHandle(String uri) {
        try {
            URL url = new URL(uri);
            return url.getHost().equals(getHandledHost());
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
