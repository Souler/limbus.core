package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class VideoScraperTest {

    abstract protected String[] getAliveLiks();
    abstract protected String[] getDeadLinks();
    abstract protected VideoSiteScraper getVideoScraper();

    protected String performGet(VideoSiteScraper scraper, String uri) throws IOException, ScrapingException {
        return scraper.get(uri);
    }

    @Test
    public void getAliveLinksShouldReturnValidUrls() {
        VideoSiteScraper scraper = getVideoScraper();

        for (String url : getAliveLiks()) {
            try {
                String result = performGet(scraper, url);
                assertNotNull("The returned result by the scraper was null", result);
                assertTrue("The returned result was not a valid URI", isValidUri(result));
            } catch (IOException e) {
                fail(e.getMessage());
            } catch (ScrapingException e) {
                fail("Error while getting " + url + "\n" + e.getMessage());
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
