package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class PowvideoScraperTest extends ParametrizedVideoScraperTest {

    public PowvideoScraperTest(String url, boolean isAlive) {
        super(new PowvideoScraper(), false, url, isAlive);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://powvideo.net/ilfc4hg4oqg2", true},
                {"http://powvideo.net/khss6vzfr8sd", true}
        });
    }
}