package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.video.base.EvalVideoScraper;

public class StreameScraper extends EvalVideoScraper {
    @Override
    public String getHandledHost() {
        return "streame.net";
    }
}
