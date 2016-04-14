package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;

public class StreaminToScraperTest extends VideoScraperTest {

    @Override
    protected String[] getAliveLiks() {
        return new String[]{
                "http://streamin.to/vl7d79kfv0g9"
        };
    }

    @Override
    protected String[] getDeadLinks() {
        return new String[]{
                "http://streamin.to/ik4h8vw4ufxw"
        };
    }

    @Override
    protected VideoSiteScraper getVideoScraper() {
        return new StreaminToScraper();
    }
}
