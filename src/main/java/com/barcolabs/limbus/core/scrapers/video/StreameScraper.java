package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.video.base.EvalVideoScraper;

/**
 * Created by Juan José on 26/03/2016.
 */
public class StreameScraper  extends EvalVideoScraper {
    @Override
    public String getHandledHost() {
        return "streame.net";
    }
}
