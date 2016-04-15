package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class NowvideoScraperTest extends ParametrizedVideoScraperTest {

    public NowvideoScraperTest(String url, boolean isAlive) {
        super(new NowvideoScraper(), false, url, isAlive);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://www.nowvideo.sx/video/0e679d454f3b6", true},
        });
    }
}