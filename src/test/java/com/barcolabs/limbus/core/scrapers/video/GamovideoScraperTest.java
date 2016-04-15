package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class GamovideoScraperTest extends ParametrizedVideoScraperTest {

    public GamovideoScraperTest(String url, boolean isAlive) {
        super(new GamovideoScraper(), false, url, isAlive);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://gamovideo.com/63h5k524n936", true},
                {"http://gamovideo.com/3lsucyfv2poz", false},
        });
    }
}