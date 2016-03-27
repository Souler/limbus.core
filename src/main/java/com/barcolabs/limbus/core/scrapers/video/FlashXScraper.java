package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.scrapers.video.base.EvalVideoScraper;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by barbosa on 1/09/15.
 */
public class FlashXScraper extends EvalVideoScraper {

    @Override
    public String getHandledHost() {
        return "www.flashx.tv";
    }

    @Override
    protected String filterResources(List<String> resources) {
        for (String resource : resources) {
            Pattern p = Pattern.compile("^http:.*\\.mp4$");
            if (p.matcher(resource).find())
                return resource;
        }
        return null;
    }

    @Override
    protected String parsePostedDocument(Document doc) throws ScrapingException {
        try {
            return super.parsePostedDocument(doc);
        } catch (ScrapingException e) {
            if (e.getMessage().equals("Skipped countdown")) {
                System.out.println("FlashX: got skipped countdowm");
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
