package com.barcolabs.limbus.core.content;

import com.barcolabs.limbus.core.scrapers.ContentSiteScraper;

public class VideoSiteLink {

    private ContentSiteScraper scraper;
    private String uri;
    private String language;
    private String subtitles;
    private String quality;

    public VideoSiteLink(ContentSiteScraper scraper, String uri, String language, String subtitles, String quality) {
        this.scraper = scraper;
        this.uri = uri;
        this.language = language;
        this.subtitles = subtitles;
        this.quality = quality;
    }

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
