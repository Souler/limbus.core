package com.barcolabs.limbus.core.scrapers.video;

import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.exceptions.UnexpectedStructureException;
import com.barcolabs.limbus.core.scrapers.VideoSiteScraper;
import com.barcolabs.limbus.core.scrapers.video.base.GetAndPostVideoScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NowvideoScraper extends GetAndPostVideoScraper {

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
        RawHttpClient client = new RawHttpClient();

        StringBuilder url = new StringBuilder(API_ENDPOINT + '?');
        for (int i=0; i < params.length; i++) {
            String[] pair = params[i];
            url.append(URLEncoder.encode(pair[0], "UTF-8"));
            url.append("=");
            url.append(URLEncoder.encode(pair[1], "UTF-8"));
            if (i != params.length - 1)
                url.append("&");
        }

        String response = client.get(url.toString());
        String[] queryParams = response.split("&");
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
        RawHttpClient client = new RawHttpClient();
        return Jsoup.parse(client.get(location), location);

    }

    @Override
    protected Document postForm(String location, ArrayList<String[]> params) throws IOException, ScrapingException {
        RawHttpClient client = new RawHttpClient();
        return Jsoup.parse(client.post(location, params), location);
    }

    @Override
    protected String parsePostedDocument(Document doc) throws ScrapingException, IOException {
        String[][] params = findApiParams(doc);
        return getVideoUrlFromApi(params);
    }

    private static  class RawHttpClient {

        private static InetAddress add;

        public RawHttpClient() {

        }

        public String get(String location) throws IOException {
            return this.exec("GET", location, null);
        }

        public String post(String location,  ArrayList<String[]> data) throws IOException {
            return this.exec("POST", location, data);
        }

        public String exec(String method, String location, ArrayList<String[]> data) throws IOException {
            URL url = new URL(location);

            InetAddress address = this.add;
            int port = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();

            // Find a suitable address accepting http traffic
            if (this.add == null) {
                for(InetAddress add : InetAddress.getAllByName(url.getHost())) {
                    try {
                        // Prepare the request
                        Socket s =  new Socket(add, port);
                        s.getInputStream().close();
                        address = add;
                        this.add = add;
                    } catch (ConnectException e) {}
                }
            }

            Socket s =  new Socket(address, port);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println(method.toUpperCase() + " " + url.getPath() + '?' + url.getQuery() + " HTTP/1.1");
            pw.println("Host: " + url.getHost());
            pw.println("User-Agent: " + USER_AGENT);
            pw.println("Referer: " + location);
            if (data != null) {
                StringBuilder params = new StringBuilder();
                for (int i=0; i<data.size(); i++) {
                    String[] pair = data.get(i);
                    params.append(URLEncoder.encode(pair[0], "UTF-8"));
                    params.append("=");
                    params.append(URLEncoder.encode(pair[1], "UTF-8"));
                    if (i != data.size() - 1)
                        params.append("&");
                }
                pw.println("Content-Type: application/x-www-form-urlencoded");
                pw.println("Content-Length: " + params.length());
                pw.println("");
                pw.println(params);
            }
            else
                pw.println("");

            pw.flush();

            // Recieve the response
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            StringBuilder body = new StringBuilder();
            String line;
            // "Parse" header response
            while ((line = br.readLine()) != null && line.length() != 0) {}
            // "Read" The chunk size
            br.readLine();
            // Read the body
            while ((line = br.readLine()) != null) body.append(line + "\r\n");
            br.close();
            pw.close();

            return body.toString();
        }
    }
}
