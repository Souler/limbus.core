package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class StreamCloudScraperTest extends ParametrizedVideoScraperTest {

    public StreamCloudScraperTest(String url, boolean isAlive) {
        super(new StreamCloudScraper(), true, url, isAlive);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://streamcloud.eu/f9c2tj25ci43", true},
        });
    }
}