package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;

public class GamovideoScraperTest extends VideoScraperTest {

    @Override
    protected String[] getAliveLiks() {
        return new String[] {
                "http://gamovideo.com/63h5k524n936"
        };
    }

    @Override
    protected String[] getDeadLinks() {
        return new String[] {
                "http://gamovideo.com/3lsucyfv2poz"
        };
    }

    @Override
    protected VideoSiteScraper getVideoScraper() {
        return new GamovideoScraper();
    }
}
