package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.scrapers.video.base.EvalVideoScraper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class IDoWatchScraper extends EvalVideoScraper {
    @Override
    public String getHandledHost() {
        return "idowatch.net";
    }

    @Override
    public String get(String location) throws IOException, ScrapingException {
        Document docGet = getDocument(location);
        return parsePostedDocument(docGet);
    }

}
