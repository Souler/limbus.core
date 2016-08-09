package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class StreameScraperTest extends ParametrizedVideoScraperTest {

    public StreameScraperTest(String url, boolean isAlive) {
        super(new StreameScraper(), false, url, isAlive);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://streame.net/v0lee8ft573z", true},
                {"http://streame.net/vzdzej7a8cj3", false},
                {"http://streame.net/7dfhq8oqr0e2", false},
        });
    }
}