package com.barcolabs.limbus.core.scrapers.video.base;

import com.barcolabs.limbus.core.exceptions.*;
import com.barcolabs.limbus.core.scrapers.video.js.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by barbosa on 1/09/15.
 */
public abstract class EvalVideoScraper extends GetAndPostVideoScraper {

    public static ScrapingJavaScriptEngine getEngine() {
        return new ScrapingJavaScriptEngine();
    }

    public static String getCodeToEval(Document doc) throws ScrapingException {
        Elements scripts = doc.select("script");
        String targetScript = null;
        for (Element script : scripts) {
            String html = script.html().trim();
            if (html.indexOf("eval") == 0) {
                return html.substring("eval".length(), html.length());
            } else if (html.indexOf("jwplayer(") == 0) {
                return html;
            }
        }
        throw new UnexpectedStructureException("Couldn't find eval script tag.");
    }

    protected String filterResources(List<String> resources) {
        return resources.size() > 0 ? resources.get(0) : null;
    }

    protected String findResource(ScrapingJavaScriptEngine engine) throws ScrapingException {
        List<String> resources = engine.getJwPlayer().getFiles();
        String result = filterResources(resources);

        if (result == null)
            throw new ScrapingException("Couldn't find any resource");

        return result;
    }

    @Override
    protected String parsePostedDocument(Document doc) throws ScrapingException {

        checkForErrors(doc);

        String toEvalGenerator = getCodeToEval(doc);
        ScrapingJavaScriptEngine engine = getEngine();
        Object interpreted = engine.eval(toEvalGenerator);

        if (interpreted instanceof String) {
            engine.eval((String) interpreted);
        }
        return findResource(engine);
    }


}
