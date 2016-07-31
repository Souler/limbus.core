package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class StreaminToScraperTest extends ParametrizedVideoScraperTest {

    public StreaminToScraperTest(String url, boolean isAlive) {
        super(new StreaminToScraper(), false, url, isAlive);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://streamin.to/eri713y5j58g", true},
                {"http://streamin.to/vl7d79kfv0g9", true},
                {"http://streamin.to/ik4h8vw4ufxw", false},
        });
    }
}