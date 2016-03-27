package com.barcolabs.limbus.core.content;

import com.barcolabs.limbus.core.scrapers.ContentSiteScraper;

import java.text.Normalizer;

public class Series implements Searchable {

    private ContentSiteScraper scraper;
    private String id;
    private String poster;
    private String title;

    public Series(ContentSiteScraper scraper, String id, String poster, String title) {
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.scraper = scraper;
    }

    public Type getType() {
        return Type.SERIES;
    }

    public String getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public ContentSiteScraper getScraper() {
        return scraper;
    }

    public int hashCode() {
        return Normalizer.normalize(title, Normalizer.Form.NFD)
                .trim()
                .toLowerCase()
                .replace("[^a-z0-9]", "")
                .hashCode();
    }
}
