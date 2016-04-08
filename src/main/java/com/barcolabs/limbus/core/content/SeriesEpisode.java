package com.barcolabs.limbus.core.content;

import com.barcolabs.limbus.core.scrapers.ContentSiteScraper;

import java.io.Serializable;

public class SeriesEpisode implements Serializable, Watchable {

    private ContentSiteScraper scraper;
    private Series series;
    private String id;
    private int season;
    private int episode;
    private String title;
    private String thumbnail;

    public SeriesEpisode(ContentSiteScraper scraper, Series series, String id, int season, int episode, String title, String thumbnail) {
        this.scraper = scraper;
        this.series =  series;
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

    public Series getSeries() {
        return series;
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
