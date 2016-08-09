package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;
import com.barcolabs.limbus.core.scrapers.content.HDFullScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HDFullExternalScraper extends VideoSiteScraper {
    @Override
    public String get(String uri) throws IOException, ScrapingException {
        Connection connWeb = Jsoup.connect(uri)
                .method(Connection.Method.GET)
                .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0")
                .referrer(HDFullScraper.URL_HOME)
                .followRedirects(true);

        Connection.Response response = connWeb.execute();
        Document doc = response.parse();
        Elements leaveButton = doc.select("a.btn.btn-info");
        String extUrl = leaveButton.attr("href");

        VideoSiteScraper externalScraper = VideoSiteScraper.getScraperFor(extUrl);
        if (externalScraper == null)
            return null;

        return externalScraper.get(extUrl);
    }

    @Override
    public String getHandledHost() {
        return HDFullScraper.URL_HOME;
    }

    @Override
    public boolean canHandle(String uri) {
        return uri.startsWith("http://hdfull.tv/ext/") ;
    }
}
