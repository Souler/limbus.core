package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.exceptions.UnexpectedStructureException;
import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NowvideoScraper extends VideoSiteScraper {

    private final static String API_ENDPOINT = "http://www.nowvideo.sx/api/player.api.php";

    private final static String[][] API_PARAMS = {
            {"key", "fkzd"},
            {"file", "flashvars.file"},
            {"cid1", "flashvars.cid1"},
            {"cid2", "flashvars.cid2"},
            {"cid3", "flashvars.cid3"},
            {"user", "user"},
            {"pass", "pass"},
            {"numOfErrors", "0"}
    };

    @Override
    public String getHandledHost() {
        return "www.nowvideo.sx";
    }

    private String[][] findApiParams(Document doc) throws ScrapingException {
        Elements scripts = doc.select("script");
        Element targetScript = null;

        for (Element script : scripts) {
            if (script.html().contains("flashvars")) {
                targetScript = script;
                break;
            }
        }

        if (targetScript == null)
            throw new UnexpectedStructureException("Couldn't find eval script tag.");

        Pattern keyRegex = Pattern.compile("var fkzd=\"(.*?)\"");
        Pattern fileRegex = Pattern.compile("flashvars.file=\"(.*?)\";");

        Matcher keyMatcher = keyRegex.matcher(targetScript.html());
        Matcher fileMatcher = fileRegex.matcher(targetScript.html());

        if (!keyMatcher.find())
            throw new UnexpectedStructureException("Couldn't find eval script tag.");

        if (!fileMatcher.find())
            throw new UnexpectedStructureException("Couldn't find file for nowvideo api.");

        return new String[][]{
                new String[]{"key", keyMatcher.group(1)},
                new String[]{"file", fileMatcher.group(1)},
        };
    }

    private String getVideoUrlFromApi(String[][] params) throws IOException, ScrapingException {
        Connection conn = Jsoup.connect(API_ENDPOINT)
                .ignoreContentType(true)
                .userAgent(USER_AGENT)
                .followRedirects(true);

        for (String[] param : params) {
            String key = param[0];
            String value = param[1];
            conn.data(key, value);
        }

        Connection.Response response = conn.execute();
        String[] queryParams = response.body().split("&");
        for (String param : queryParams) {
            String[] pair = param.split("=");
            String key = pair[0];
            String value = pair[1];
            if (key.equals("url"))
                return value;
        }

        throw new ScrapingException("No url param found on API respone");
    }

    protected Document getDocument(String location) throws IOException, ScrapingException {
        Connection.Response response = Jsoup.connect(location)
                .referrer(location)
                .ignoreContentType(true)
                .userAgent(USER_AGENT)
                .followRedirects(true)
                .execute();

        return response.parse();
    }

    @Override
    public String get(String uri) throws IOException, ScrapingException {
        Document doc = getDocument(uri);
        String[][] params = findApiParams(doc);
        return getVideoUrlFromApi(params);
    }
}
