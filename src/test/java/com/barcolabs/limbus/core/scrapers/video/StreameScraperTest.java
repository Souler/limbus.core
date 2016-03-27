package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;

/**
 * Created by Juan José on 26/03/2016.
 */
public class StreameScraperTest extends VideoScraperTest{

    @Override
    protected String[] getAliveLiks() {
        return new String[] {
                "http://streame.net/7dfhq8oqr0e2"
        };
    }

    @Override
    protected String[] getDeadLinks() {
        return new String[] {
                // TODO: Research
        };
    }

    @Override
    protected VideoSiteScraper getVideoScraper() {
        return new StreameScraper();
    }
}