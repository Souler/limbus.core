package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;

public class StreamCloudScraperTest extends VideoScraperTest{

    @Override
    protected boolean useProxy() { return true; }

    @Override
    protected String[] getAliveLiks() {
        return new String[] {
                "http://streamcloud.eu/f9c2tj25ci43"
        };
    }

    @Override
    protected String[] getDeadLinks() {
        return new String[] {
                // TODO: Research them
        };
    }

    @Override
    protected VideoSiteScraper getVideoScraper() {
        return new StreamCloudScraper();
    }
}
