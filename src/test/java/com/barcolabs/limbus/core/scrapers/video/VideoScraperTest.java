package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static org.junit.Assert.*;

public abstract class VideoScraperTest {

    abstract protected String[] getAliveLiks();
    abstract protected String[] getDeadLinks();
    abstract protected VideoSiteScraper getVideoScraper();

    protected String performGet(VideoSiteScraper scraper, String uri) throws IOException, ScrapingException {
        return scraper.get(uri);
    }

    protected boolean useProxy() { return false; }

    @Before
    public void setProxy() throws Exception {

        String ipInitial = Jsoup.connect("http://api.ipify.org/?format=plain").get().body().html();
        assertNotNull(ipInitial);

        if (!this.useProxy()) {
            System.setProperty("http.proxyHost", "");
            System.setProperty("https.proxyHost", "");
            System.setProperty("http.proxyPort", "");
            System.setProperty("https.proxyPort", "");
            return;
        }

        Map<String, String> env = System.getenv();
        String proxyHost = env.get("PROXY_HOST");
        String proxyPort = env.get("PROXY_PORT");

        if (proxyHost != null) {
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("https.proxyHost", proxyHost);
        }

        if (proxyPort != null) {
            System.setProperty("http.proxyPort", proxyPort);
            System.setProperty("https.proxyPort", proxyPort);
        }

        String ipBehindProxy = Jsoup.connect("http://api.ipify.org/?format=plain").get().body().html();
        assertNotNull(ipBehindProxy);
        assertNotEquals(ipInitial, ipBehindProxy);
    }

    @Test
    public void getAliveLinksShouldReturnValidUrls() {
        VideoSiteScraper scraper = getVideoScraper();

        for (String url : getAliveLiks()) {
            try {
                String result = performGet(scraper, url);
                assertNotNull("The returned result by the scraper was null", result);
                assertTrue("The returned result was not a valid URI", isValidUri(result));
            } catch (Exception e) {
                fail("Error while getting " + url + "\n" + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private boolean isValidUri(String uri) {
        try {
            URI.create(uri);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
