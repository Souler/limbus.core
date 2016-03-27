package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;

/**
 * Created by Juan Jos� on 26/03/2016.
 */
public class StreamPlayScraperTest extends VideoScraperTest {
    @Override
    protected String[] getAliveLiks() {
        return new String[] {
                "http://streamplay.to/m1wwqwdkkwo5"
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
        return new StreamPlayScraper();
    }
}
