package com.barcolabs.limbus.core.scrapers;

import com.barcolabs.limbus.core.content.*;
import com.barcolabs.limbus.core.exceptions.ScrapingException;

import java.io.IOException;

public abstract class ContentSiteScraper extends Scraper {
    public abstract String getName();

    public abstract Searchable[] search(String query) throws ScrapingException, IOException;

    public abstract Searchable[] getFeatured() throws ScrapingException, IOException;

    public abstract SeriesEpisode[] getEpisodesForSeries(String seriesId) throws ScrapingException, IOException;

    public abstract VideoSiteLink[] getVideoSiteLinks(String watchableId) throws ScrapingException, IOException;

    public SeriesEpisode[] getEpisodesForSeries(Series series) throws ScrapingException, IOException {
        return getEpisodesForSeries(series.getId());
    }

    public VideoSiteLink[] getVideoSiteLinks(Watchable watchable) throws ScrapingException, IOException {
        return getVideoSiteLinks(watchable.getId());
    }
}
