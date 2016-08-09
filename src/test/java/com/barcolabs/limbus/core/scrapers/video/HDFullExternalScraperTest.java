package com.barcolabs.limbus.core.scrapers.video;

import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

public class HDFullExternalScraperTest extends ParametrizedVideoScraperTest {
    public HDFullExternalScraperTest(String url, boolean isAlive) {
        super(new HDFullExternalScraper(), false, url, isAlive);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http://hdfull.tv/ext/aHR0cDovL3Bvd3ZpZGVvLm5ldC8wNTg2bGtldWZ3aTA=", true},
        });
    }
}
