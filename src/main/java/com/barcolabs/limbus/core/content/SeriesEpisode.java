package com.barcolabs.limbus.core.content;

import com.barcolabs.limbus.core.scrapers.ContentSiteScraper;

public class SeriesEpisode {

    private ContentSiteScraper scraper;
    private String id;
    private int season;
    private int episode;
    private String title;
    private String thumbnail;

    public SeriesEpisode(ContentSiteScraper scraper, String id, int season, int episode, String title, String thumbnail) {
        this.scraper = scraper;
        this.id = id;
        this.season = season;
        this.episode = episode;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public ContentSiteScraper getScraper() {
        return scraper;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
