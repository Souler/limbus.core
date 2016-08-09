package com.barcolabs.limbus.core.scrapers;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.scrapers.video.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class VideoSiteScraper extends Scraper {

    private static VideoSiteScraper[] scrapers = new VideoSiteScraper[] {
            new HDFullExternalScraper(),
            new FlashXScraper(),
            new GamovideoScraper(),
            new NowvideoScraper(),
            new PowvideoScraper(),
            new StreamCloudScraper(),
            new StreameScraper(),
            new StreaminToScraper(),
            new StreamPlayScraper(),
            new IDoWatchScraper()
    };

    public static VideoSiteScraper getScraperFor(String videoSiteUrl) {
        for (VideoSiteScraper scraper : scrapers)
            if (scraper.canHandle(videoSiteUrl))
                return scraper;

        return null;
    }

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
