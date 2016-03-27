package com.barcolabs.limbus.core.scrapers;

import com.barcolabs.limbus.core.content.Searchable;
import com.barcolabs.limbus.core.content.Series;
import com.barcolabs.limbus.core.content.SeriesEpisode;
import com.barcolabs.limbus.core.content.VideoSiteLink;
import com.barcolabs.limbus.core.exceptions.ScrapingException;

import java.io.IOException;

/**
 * Created by Juan José on 25/03/2016.
 */
public abstract class ContentSiteScraper extends Scraper {
    public abstract String getName();
    public abstract Searchable[] search(String query) throws ScrapingException, IOException;
    public abstract Searchable[] getFeatured() throws ScrapingException, IOException;
    public abstract SeriesEpisode[] getEpisodesForSeries(String seriesId) throws ScrapingException, IOException;
    public abstract VideoSiteLink[] getExternalResources(String episodeId) throws ScrapingException, IOException;
    public SeriesEpisode[] getEpisodesForSeries(Series series) throws ScrapingException, IOException {
        return getEpisodesForSeries(series.getId());
    }

    public VideoSiteLink[] getExternalResources(SeriesEpisode episode) throws ScrapingException, IOException {
        return getExternalResources(episode.getId());
    }
}
