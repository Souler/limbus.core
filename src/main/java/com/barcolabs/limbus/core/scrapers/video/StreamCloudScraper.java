package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.UnexpectedStructureException;
import com.barcolabs.limbus.core.scrapers.video.base.GetAndPostVideoScraper;

import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by barbosa on 30/08/15.
 */
public class StreamCloudScraper extends GetAndPostVideoScraper {

    @Override
    public String getHandledHost() {
        return "streamcloud.eu";
    }

    protected String parsePostedDocument(Document doc) throws UnexpectedStructureException {
        Pattern rgxFile = Pattern.compile("file: \"(.*)\",");
        Matcher mtcFile = rgxFile.matcher(doc.html());
        if (mtcFile.find())
            return mtcFile.group(1);
        else
            throw new UnexpectedStructureException();
    }
}
