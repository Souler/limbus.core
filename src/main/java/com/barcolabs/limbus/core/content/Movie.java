package com.barcolabs.limbus.core.content;

import com.barcolabs.limbus.core.scrapers.ContentSiteScraper;

import java.io.Serializable;

public class Movie implements Searchable, Serializable, Watchable {

    private ContentSiteScraper scraper;
    private String id;
    private String poster;
    private String title;

    public Movie(ContentSiteScraper scraper, String id, String poster, String title) {
        this.id = id;
        this.poster = poster;
        this.title = title;
        this.scraper = scraper;
    }

    public Type getType() {
        return Type.MOVIE;
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
}
