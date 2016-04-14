package com.barcolabs.limbus.core.scrapers.video;


import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;

public class PowvideoScraperTest extends VideoScraperTest {

    @Override
    protected String[] getAliveLiks() {
        return new String[]{
                "http://powvideo.net/khss6vzfr8sd",
                "http://powvideo.net/zbunw0aua6de",
                "http://powvideo.net/txguvu3w9k0w"
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
        return new PowvideoScraper();
    }
}
