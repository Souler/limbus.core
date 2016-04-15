package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public abstract class ParametrizedVideoScraperTest {

    private VideoSiteScraper scraper;
    private boolean useProxy;
    private String url;
    private boolean isAlive;

    public ParametrizedVideoScraperTest(VideoSiteScraper scraper, boolean useProxy, String url, boolean isAlive) {
        this.scraper = scraper;
        this.useProxy = useProxy;
        this.url = url;
        this.isAlive = isAlive;
    }

    private boolean isValidUri(String uri) {
        try {
            URI.create(uri);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Before
    public void setProxy() throws Exception {
        String ipApiUrl = "http://api.ipify.org/?format=plain";
        String ipInitial = Jsoup.connect(ipApiUrl).get().body().html();
        assertNotNull(ipInitial);

        if (!this.useProxy) {
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

            if (proxyPort == null)
                proxyPort = "80";

            // HTTP Proxy
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", proxyPort);
            // HTTPS Proxy
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", proxyPort);
            // Check the proxy is actually working
            String ipBehindProxy = Jsoup.connect(ipApiUrl).get().body().html();
            assertNotNull(ipBehindProxy);
            assertNotEquals(ipInitial, ipBehindProxy);
        }
    }

    @Test
    public void testFindScraperByUrl() {
        VideoSiteScraper scraper = VideoSiteScraper.getScraperFor(this.url);
        assertEquals(this.scraper.getClass(), scraper.getClass());
    }

    @Test
    public void testLink() {
        try {
            String url = scraper.get(this.url);
            if (isAlive) {
                assertNotNull("The returned result by the scraper was null", url);
                assertTrue("The returned result was not a valid URI", isValidUri(url));
            } else {
                fail("A dead link didn't throw any exception");
            }
        } catch (IOException | ScrapingException e) {
            if (!isAlive) {
                assertNotNull(e.getMessage());
            } else {
                throw new AssertionError("Alive link did throw an exception", e);
            }
        }
    }
}
