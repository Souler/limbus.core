package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.scrapers.video.base.EvalVideoScraper;

import java.util.List;
import java.util.regex.Pattern;

public class FlashXScraper extends EvalVideoScraper {

    @Override
    public String getHandledHost() {
        return "www.flashx.tv";
    }

    @Override
    protected String filterResources(List<String> resources) {
        for (String resource : resources) {
            Pattern p = Pattern.compile("^http:.*\\.mp4$");
            if (p.matcher(resource).find())
                return resource;
        }
        return null;
    }
}
