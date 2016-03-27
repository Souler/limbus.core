package com.barcolabs.limbus.core.scrapers.video;

        import com.barcolabs.limbus.core.exceptions.ScrapingException;
        import com.barcolabs.limbus.core.exceptions.UnexpectedStructureException;
        import com.barcolabs.limbus.core.scrapers.video.base.GetAndPostVideoScraper;
        import com.barcolabs.limbus.core.scrapers.video.js.ScrapingJavaScriptEngine;

        import org.jsoup.Connection;
        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.Element;
        import org.jsoup.select.Elements;

        import java.io.IOException;
        import java.net.MalformedURLException;
        import java.net.URL;

/**
 * Created by barbosa on 1/09/15.
 */
public class NowvideoScraper extends GetAndPostVideoScraper {

    private final static String API_ENDPOINT = "http://www.nowvideo.sx/api/player.api.php";

    private final static String[] swfObjectMethods ={
            "createCSS",
            "embedSWF"
    };

    private final static String[][] API_PARAMS = {
            { "key", "fkzd" },
            { "file", "flashvars.file" },
            { "cid1", "flashvars.cid1"},
            { "cid2", "flashvars.cid2"},
            { "cid3", "flashvars.cid3"},
            { "user", "user" },
            { "pass", "pass" },
            { "numOfErrors", "0" }
    };

    private final String[] HOSTNAMES = {
            "www.nowvideo.sx",
            "www.nowvideo.ch"
    };

    @Override
    public String getHandledHost() {
        return HOSTNAMES[0];
    }

    @Override
    public boolean canHandle(String uri) {
        // Override the super implementation for allow to check for two hostnames
        for (String hostname : HOSTNAMES) {
            try {
                URL url = new URL(uri);
                if (url.getHost().equals(hostname))
                    return true;
            } catch (MalformedURLException e) {}
        }
        return false;

    }

    private ScrapingJavaScriptEngine prepareEngine(Document doc) throws ScrapingException {
        Elements scripts = doc.select("script");
        Element targetScript = null;
        for (Element script : scripts) {
            if (script.html().indexOf("flashvars") >= 0) {
                targetScript = script;
                break;
            }
        }

        if (targetScript == null)
            throw new UnexpectedStructureException("Couldn't find eval script tag.");

        ScrapingJavaScriptEngine engine = new ScrapingJavaScriptEngine();
        String scriptToEval = targetScript.html().replaceAll("<!--.*-->", "");
        engine.eval(scriptToEval);

        return engine;
    }

    private String getVideoUrlFromApi(ScrapingJavaScriptEngine engine) throws IOException, ScrapingException {
        Connection conn = Jsoup.connect(API_ENDPOINT)
                .ignoreContentType(true)
                .userAgent(USER_AGENT)
                .followRedirects(true);

        for (String[] param : API_PARAMS) {
            String apiParam = param[0];
            String engineParam = param[1];
            String value = "undefined";
            Object valueObj = null;
            try {
                valueObj = engine.eval(engineParam);
            } catch (RuntimeException e) {}
            if (valueObj != null)
                value = valueObj.toString();
            conn.data(apiParam, value);
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

    @Override
    protected String parsePostedDocument(Document doc) throws ScrapingException {
        ScrapingJavaScriptEngine engine = prepareEngine(doc);

        try {
            return getVideoUrlFromApi(engine);
        } catch (IOException e) {
            throw new ScrapingException("Nowvideo: Couldn't get video from internal API");
        }
    }
}
