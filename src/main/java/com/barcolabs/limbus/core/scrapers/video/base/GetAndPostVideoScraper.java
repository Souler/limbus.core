package com.barcolabs.limbus.core.scrapers.video.base;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.exceptions.UnexpectedStructureException;
import com.barcolabs.limbus.core.exceptions.VideoSiteFileException;
import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class GetAndPostVideoScraper extends VideoSiteScraper {

    protected static final MediaType URL_ENCODED = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

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

    protected Document getDocument(String location) throws IOException, ScrapingException {
        Request request = new Request.Builder()
                .url(location)
                .header("User-Agent", USER_AGENT)
                .header("Referrer", location)
                .build();
        Response response = this.client.newCall(request).execute();
        return Jsoup.parse(response.body().string(), location);
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

    protected ArrayList<String[]> findFormFields(Element form) throws ScrapingException {

        ArrayList<String[]> fields = new ArrayList<String[]>();

        Elements inputs = form.select("input");
        for (Element input : inputs) {
            if (input.attr("type").equals("hidden") || input.attr("type").equals("submit")) {
                String fieldName = input.attr("name");
                String fieldValue = input.attr("value");
                String[] field = new String[]{fieldName, fieldValue};
                fields.add(field);
            }
        }

        return fields;
    }

    protected Document postForm(String location, ArrayList<String[]> params) throws IOException, ScrapingException {

        StringBuilder bodyBuilder = new StringBuilder();
        for (int i=0; i < params.size(); i++) {
            String[] pair = params.get(i);
            bodyBuilder.append(URLEncoder.encode(pair[0], "UTF-8"));
            bodyBuilder.append("=");
            bodyBuilder.append(URLEncoder.encode(pair[1], "UTF-8"));
            if (i != params.size() - 1)
                bodyBuilder.append("&");
        }

        RequestBody body = RequestBody.create(URL_ENCODED, bodyBuilder.toString());
        Request request = new Request.Builder()
                .url(location)
                .header("User-Agent", USER_AGENT)
                .header("Referrer", location)
                .post(body)
                .build();
        Response response = this.client.newCall(request).execute();
        return Jsoup.parse(response.body().string(), location);
    }

    protected abstract String parsePostedDocument(Document doc) throws ScrapingException, IOException;

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
