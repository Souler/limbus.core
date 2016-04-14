package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.UnexpectedStructureException;
import com.barcolabs.limbus.core.scrapers.video.base.GetAndPostVideoScraper;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreaminToScraper extends GetAndPostVideoScraper {

    @Override
    public String getHandledHost() {
        return "streamin.to";
    }

    protected String parsePostedDocument(Document doc) throws UnexpectedStructureException {
        String html = doc.html();
        Pattern rgxFile = Pattern.compile("file: \"(.*)\",");
        Pattern rgxStreamer = Pattern.compile("streamer: \"(.*)\",");
        Matcher mtcFile = rgxFile.matcher(html);
        Matcher mtcStreamer = rgxStreamer.matcher(html);

        if (mtcFile.find() && mtcStreamer.find())
            return mtcStreamer.group(1) + "/_definst_/" + mtcFile.group(1);
        else
            throw new UnexpectedStructureException();
    }
}