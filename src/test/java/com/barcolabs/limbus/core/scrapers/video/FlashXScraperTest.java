package com.barcolabs.limbus.core.scrapers.video;


import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;

public class FlashXScraperTest extends VideoScraperTest {

    @Override
    protected String[] getAliveLiks() {
        return new String[]{
                "http://www.flashx.tv/gcluvdew6reo.html"
        };
    }

    @Override
    protected String[] getDeadLinks() {
        return new String[]{
                // TODO: Research them
        };
    }

    @Override
    protected VideoSiteScraper getVideoScraper() {
        return new FlashXScraper();
    }
}