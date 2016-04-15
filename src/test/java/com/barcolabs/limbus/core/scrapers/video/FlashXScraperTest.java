package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class FlashXScraperTest extends ParametrizedVideoScraperTest {

    public FlashXScraperTest(String url, boolean isAlive) {
        super(new FlashXScraper(), false, url, isAlive);
    }


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://www.flashx.tv/gcluvdew6reo.html", true},
        });
    }
}