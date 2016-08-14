package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;
import com.barcolabs.limbus.core.scrapers.video.base.EvalVideoScraper;
import com.barcolabs.limbus.core.scrapers.video.base.GetAndPostVideoScraper;
import com.barcolabs.limbus.core.scrapers.js.ScrapingJavaScriptEngine;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class GamovideoScraper extends VideoSiteScraper {

    @Override
    public String getHandledHost() {
        return "gamovideo.com";
    }

    private Document getDocument(String location) throws IOException {
        Connection.Response response = Jsoup.connect(location)
                .ignoreContentType(true)
                .userAgent(USER_AGENT)
                .followRedirects(true)
                .execute();

        return response.parse();
    }

    private String findUrl(Document doc) throws ScrapingException {

        GetAndPostVideoScraper.checkForErrors(doc);

        String code = EvalVideoScraper.getCodeToEval(doc);
        ScrapingJavaScriptEngine engine = EvalVideoScraper.getEngine();
        engine.eval(code);

        String result = engine.getJwPlayer().getFile();
        if (result == null)
            throw new ScrapingException("Couldn't jwplayer getFile");
        return result;
    }

    @Override
    public String get(String uri) throws IOException, ScrapingException {
        Document doc = getDocument(uri);
        return findUrl(doc);
    }
}
