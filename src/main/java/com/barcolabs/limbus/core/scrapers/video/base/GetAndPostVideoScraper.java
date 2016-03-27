package com.barcolabs.limbus.core.scrapers.video.base;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.exceptions.UnexpectedStructureException;
import com.barcolabs.limbus.core.exceptions.VideoSiteFileException;

import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by barbosa on 31/08/15.
 */
public abstract class GetAndPostVideoScraper extends VideoSiteScraper {

    protected Document getDocument(String location) throws IOException, ScrapingException {
        Connection.Response response= Jsoup.connect(location)
                .referrer(location)
                .ignoreContentType(true)
                .userAgent(USER_AGENT)
                .followRedirects(true)
                .execute();

        return response.parse();
    }

    public static void checkForErrors(Document doc) throws ScrapingException {
        if (doc.html().indexOf("File Deleted.") >= 0)
            throw new VideoSiteFileException("File Deleted.");

        Element error = doc.select("b.err").first();
        if (error != null)
            throw new VideoSiteFileException(error.text());

        Element error2 = doc.select("#file p").first();
        if (error2 != null)
            throw new VideoSiteFileException(error2.text());
    }

    protected Element findForm(Document doc) throws ScrapingException {

        checkForErrors(doc);

        Element form = doc.select("form[method=POST]").first();
        if (form == null)
            throw new UnexpectedStructureException(doc.baseUri());

        return form;
    }

    protected long findWaitTime(Document doc) {
        long waitTimer = 0L;
        Element timer = doc.select("#cxc").first();
        if (timer != null)
            waitTimer = Integer.parseInt(timer.text());

        Pattern rgxCount = Pattern.compile("var count = ([0-9]+)");
        Matcher mtcCount = rgxCount.matcher(doc.html());
        if (mtcCount.find())
            waitTimer = Integer.parseInt(mtcCount.group(1));

        // Safety measure
        waitTimer = (waitTimer + 1) * 1000;
        return waitTimer;
    }

    protected String findPostURL(Element form) {
        return form.absUrl("action");
    }

    protected ArrayList<String[]> findFormFields(Element form) throws ScrapingException{

        ArrayList<String[]> fields = new ArrayList<String[]>();

        Elements inputs = form.select("input");
        for (Element input : inputs) {
            if (input.attr("type").equals("hidden") || input.attr("type").equals("submit")) {
                String fieldName = input.attr("name");
                String fieldValue = input.attr("value");
                String[] field = new String[] { fieldName, fieldValue };
                fields.add(field);
            }
        }

        return fields;
    }

    protected Document postForm(String location, ArrayList<String[]> params) throws IOException, ScrapingException {
        Connection conn = Jsoup.connect(location)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .userAgent(USER_AGENT)
                .referrer(location)
                .followRedirects(true);

        for(String[] fieldPair : params)
            conn.data(fieldPair[0], fieldPair[1]);

        Connection.Response response = conn.execute();
        Document doc = response.parse();

        return doc;
    }

    protected abstract String parsePostedDocument(Document doc) throws ScrapingException;

    public String get(String location) throws IOException, ScrapingException {
        Document docGet = getDocument(location);
        Element form = findForm(docGet);
        String postURL = findPostURL(form);
        ArrayList<String[]> fields = findFormFields(form);
        long waitTime = findWaitTime(docGet);

        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Document docPost = postForm(postURL, fields);
        return parsePostedDocument(docPost);
    }
}
