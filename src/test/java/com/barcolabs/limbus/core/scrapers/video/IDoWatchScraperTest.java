package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class IDoWatchScraperTest extends ParametrizedVideoScraperTest {

    public IDoWatchScraperTest(String url, boolean isAlive) {
        super(new IDoWatchScraper(), false, url, isAlive);
    }


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://idowatch.net/yha3zl6wq7gh.html", true},
        });
    }
}