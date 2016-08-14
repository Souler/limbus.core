package com.barcolabs.limbus.core.scrapers.video.base;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.exceptions.UnexpectedStructureException;
import com.barcolabs.limbus.core.scrapers.js.ScrapingJavaScriptEngine;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public abstract class EvalVideoScraper extends GetAndPostVideoScraper {

    public static ScrapingJavaScriptEngine getEngine() {
        return new ScrapingJavaScriptEngine();
    }

    public static String getCodeToEval(Document doc) throws ScrapingException {
        Elements scripts = doc.select("script");

        for (Element script : scripts) {
            String html = script.html().trim();
            if (html.indexOf("eval") == 0 || html.indexOf("jwplayer(") == 0) {
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
    protected String parsePostedDocument(Document doc) throws ScrapingException, IOException {

        try {
            checkForErrors(doc);
        } catch (ScrapingException e) {
            if (e.getMessage().toLowerCase().equals("skipped countdown")) {
                System.out.println(this.getClass().getSimpleName() + ": got skipped countdowm");
                return this.get(doc.location());
            } else
                throw e;
        }

        String code = getCodeToEval(doc);
        ScrapingJavaScriptEngine engine = getEngine();
        engine.eval(code);
        return findResource(engine);
    }


}
