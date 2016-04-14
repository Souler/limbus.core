package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;

public class NowvideoScraperTest extends VideoScraperTest {
    @Override
    protected String[] getAliveLiks() {
        return new String[]{
                "http://www.nowvideo.sx/video/0e679d454f3b6"
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
        return new NowvideoScraper();
    }
}
