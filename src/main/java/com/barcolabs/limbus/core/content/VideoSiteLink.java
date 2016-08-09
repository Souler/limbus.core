package com.barcolabs.limbus.core.content;

import com.barcolabs.limbus.core.scrapers.ContentSiteScraper;

import java.io.Serializable;

public class VideoSiteLink implements Serializable {

    private ContentSiteScraper scraper;
    private String provider;
    private String uri;
    private String language;
    private String subtitles;
    private String quality;

    public VideoSiteLink(ContentSiteScraper scraper, String provider, String uri, String language, String subtitles, String quality) {
        this.scraper = scraper;
        this.provider = provider;
        this.uri = uri;
        this.language = language;
        this.subtitles = subtitles;
        this.quality = quality;
    }

    public String getProvider() { return this.provider; }

    public ContentSiteScraper getScraper() {
        return scraper;
    }

    public String getUri() {
        return uri;
    }

    public String getLanguage() {
        return language;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public String getQuality() {
        return quality;
    }
}
