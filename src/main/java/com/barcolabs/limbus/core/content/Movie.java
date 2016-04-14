package com.barcolabs.limbus.core.content;

import com.barcolabs.limbus.core.scrapers.ContentSiteScraper;

import java.io.Serializable;

public class Movie implements Searchable, Serializable, Watchable {

    private ContentSiteScraper scraper;
    private String id;
    private String title;
    private String poster;

    private String thumbnail;

    public Movie(ContentSiteScraper scraper, String id, String title, String poster, String thumbnail) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.thumbnail = thumbnail;
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
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
