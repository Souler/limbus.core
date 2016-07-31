package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class StreamPlayScraperTest extends ParametrizedVideoScraperTest {

    public StreamPlayScraperTest(String url, boolean isAlive) {
        super(new StreamPlayScraper(), false, url, isAlive);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://streamplay.to/3hspka903u1b", true},
                {"http://streamplay.to/m1wwqwdkkwo5", true},
        });
    }
}