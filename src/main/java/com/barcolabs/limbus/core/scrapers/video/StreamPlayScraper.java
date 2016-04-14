package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.video.base.EvalVideoScraper;

public class StreamPlayScraper extends EvalVideoScraper {
    @Override
    public String getHandledHost() {
        return "streamplay.to";
    }
}
