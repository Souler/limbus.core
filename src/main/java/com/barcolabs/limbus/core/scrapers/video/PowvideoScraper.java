package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.scrapers.video.base.EvalVideoScraper;
import com.barcolabs.limbus.core.scrapers.video.js.ScrapingJavaScriptEngine;
import org.jsoup.nodes.Document;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

import java.io.IOException;
import java.util.regex.Pattern;

public class PowvideoScraper extends EvalVideoScraper {
    @Override
    public String getHandledHost() {
        return "powvideo.net";
    }

    @Override
    protected String findResource(ScrapingJavaScriptEngine engine) throws ScrapingException {
        NativeArray sources = (NativeArray) engine.eval("sources");
        for (int i = 0; i < sources.getLength(); i++) {
            NativeObject source = (NativeObject) sources.get(i, sources);
            String file = (String) source.get("file", source);

            Pattern p = Pattern.compile("^http:.*\\.mp4$");
            if (p.matcher(file).find())
                return file;
        }
        throw new ScrapingException("Couldn't find sources[2]['file']");
    }

    @Override
    protected long findWaitTime(Document doc) {
        return super.findWaitTime(doc) * 2;
    }

    @Override
    protected String parsePostedDocument(Document doc) throws ScrapingException {
        try {
            return super.parsePostedDocument(doc);
        } catch (ScrapingException e) {
            if (e.getMessage().equals("Skipped countdown")) {
                System.out.println("Powvideo: got skipped countdowm");
                try {
                    return this.get(doc.location());
                } catch (IOException e1) {}
            }
            else
                throw e;
        }
        return null;
    }
}