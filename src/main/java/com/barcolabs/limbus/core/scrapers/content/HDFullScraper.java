package com.barcolabs.limbus.core.scrapers.content;

import com.barcolabs.limbus.core.content.*;
import com.barcolabs.limbus.core.exceptions.ScrapingException;
import com.barcolabs.limbus.core.exceptions.UnexpectedStructureException;
import com.barcolabs.limbus.core.scrapers.ContentSiteScraper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HDFullScraper extends ContentSiteScraper {
    public static final String URL_HOME = "http://hdfull.tv";
    public static final String NAME = "hdfull.tv";
    private static final String URL_SEARCH = "http://hdfull.tv/ajax/search.php";
    private static final String URL_SERIES = "http://hdfull.tv/serie/";
    private static final String URL_EPISODES = "http://hdfull.tv/a/episodes";
    private static final String URL_THUMBNAIL = "http://hdfull.tv/tthumb/220x124/";
    private Gson gson;

    public HDFullScraper() {
        this.gson = new Gson();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Searchable[] search(String query) throws ScrapingException, IOException {
        Connection conn = Jsoup.connect(URL_SEARCH)
                .ignoreContentType(true)
                .method(Connection.Method.POST)
                .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0")
                .header("Accept", "application/json, text/javascript, */*; q=0.01")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("ContentType", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("X-Requested-With", "XMLHttpRequest")
                .referrer(URL_HOME)
                .followRedirects(true);

        conn.data("q", query);
        conn.data("limit", Integer.toString(5));
        conn.data("timestamp", Long.toString(System.currentTimeMillis()));
        conn.data("verifiedCheck", "");

        Connection.Response response = conn.execute();
        String body = response.body();

        if (body == null || body.length() <= 0)
            throw new ScrapingException("Empty search response");

        SearchResultEntry[] series;

        try {
            series = gson.fromJson(body, SearchResultEntry[].class);
        } catch (JsonSyntaxException e) {
            throw new ScrapingException("Couldn't parse JSON from search response");
        }

        Searchable[] result = new Series[series.length];
        for (int i = 0; i < result.length; i++) {
            SearchResultEntry searchable = series[i];
            String id = searchable.permalink.substring(URL_SERIES.length());
            if (searchable.meta.equals("TV show"))
                result[i] = new Series(this, id, searchable.title, searchable.image);
            else
                result[i] = new Movie(this, id, searchable.title, searchable.image, null);
        }

        return result;
    }

    @Override
    public Searchable[] getFeatured() throws ScrapingException, IOException {
        HashSet<Series> result = new HashSet<Series>();

        Connection connWeb = Jsoup.connect(URL_HOME)
                .method(Connection.Method.GET)
                .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0")
                .referrer(URL_HOME)
                .followRedirects(true);

        Connection.Response res = connWeb.execute();
        Document doc = res.parse();
        Elements featured = doc.select(".flickr.item");

        for (Element element : featured) {
            Element a = element.select("a").first();
            Element img = element.select("img").first();
            String title = img.attr("original-title")
                    .replace(" [0-9]+x[0-9]+$", "");
            String poster = img.attr("src");
            String href = a.attr("href");
            String id = href.substring(URL_SERIES.length());

            // Change the url for taking the hi-res version of the poster
            poster = poster.replace("tthumb/130x190", "thumbs");

            result.add(new Series(this, id, title, poster));
        }

        return result.toArray(new Series[result.size()]);
    }

    @Override
    public SeriesEpisode[] getEpisodesForSeries(String id) throws ScrapingException, IOException {

        Connection connWeb = Jsoup.connect(URL_SERIES + id)
                .method(Connection.Method.GET)
                .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0")
                .referrer(URL_HOME)
                .followRedirects(true);

        Connection.Response res = connWeb.execute();
        Document doc = res.parse();
        String body = res.body();

        String sid;
        Pattern rgxSid = Pattern.compile("var sid = '([0-9]+)';");
        Matcher mtcSid = rgxSid.matcher(body);
        if (mtcSid.find())
            sid = mtcSid.group(1);
        else
            throw new UnexpectedStructureException("no sid found");

        Elements seasons = doc.select("#season-list li:not(:first-child) a");
        ArrayList<SeriesEpisode> episodes = new ArrayList<SeriesEpisode>();
        for (Element season : seasons) {
            Connection connApi = Jsoup.connect(URL_EPISODES)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0")
                    .header("Accept", "application/json, text/javascript, */*; q=0.01")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("ContentType", "application/x-www-form-urlencoded; charset=UTF-8")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .referrer(URL_HOME)
                    .followRedirects(true)
                    .data("action", "season")
                    .data("start", "0")
                    .data("limit", "0")
                    .data("show", sid)
                    .data("season", season.text());

            Connection.Response resApi = connApi.execute();
            EpisodesAPIResult[] apiEpisodes = gson.fromJson(resApi.body(), EpisodesAPIResult[].class);
            for (EpisodesAPIResult e : apiEpisodes) {
                String episodeId = String.format("%s/temporada-%d/episodio-%d", e.permalink, e.season, e.episode);
                String thumbnail = URL_THUMBNAIL + e.thumbnail;
                String title = String.format("%02dx%02d", e.season, e.episode);
                if (e.title.get("en") != null)
                    title = e.title.get("en");
                else if (e.title.keySet().size() > 0)
                    title = e.title.get(e.title.keySet().iterator().next());
                episodes.add(new SeriesEpisode(this, null, episodeId, e.season, e.episode, title, thumbnail));
            }
        }

        return episodes.toArray(new SeriesEpisode[episodes.size()]);
    }

    @Override
    public VideoSiteLink[] getVideoSiteLinks(String episodeId) throws ScrapingException, IOException {
        Connection connWeb = Jsoup.connect(URL_SERIES + episodeId)
                .method(Connection.Method.GET)
                .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0")
                .referrer(URL_HOME)
                .followRedirects(true);

        Connection.Response response = connWeb.execute();
        Document doc = response.parse();
        Elements links = doc.select("#embed_list > .embed-selector");
        VideoSiteLink[] result = new VideoSiteLink[links.size()];

        for (int i = 0; i < links.size(); i++) {

            // Get the external video page link
            Element link = links.get(i);
            Element anchor = link.select(".action-buttons a[target=\"_blank\"]").first();
            if (anchor == null)
                throw new UnexpectedStructureException("No anchor found");

            // Get the language
            Element langTag = link.select("h5 > span:nth-child(1)").first();
            if (langTag == null)
                throw new UnexpectedStructureException("No langTag found");
            Pattern rgxLang = Pattern.compile("idioma: ([a-záéíóúñ ]+)");
            Matcher mtcLang = rgxLang.matcher(langTag.text().toLowerCase());
            String lang;
            if (mtcLang.find())
                lang = mtcLang.group(1);
            else
                throw new UnexpectedStructureException("Can't parse langTag");

            String language = null;
            String subtitles = null;

            if (lang.equals("audio original")) {
                language = Languages.enUS;
            } else if (lang.equals("audio español")) {
                language = Languages.esES;
            } else if (lang.equals("audio latino")) {
                language = Languages.es419;
            } else if (lang.equals("subtítulo español")) {
                language = Languages.enUS;
                subtitles = Languages.esES;
            } else if (lang.equals("subtítulo ingles")) {
                language = Languages.enUS;
                subtitles = Languages.enUS;
            }

            // Get the quality
            Element qualityTag = link.select("h5 > span:nth-child(3)").first();
            if (langTag == null)
                throw new UnexpectedStructureException("No langTag found");
            Pattern rgxQuality = Pattern.compile("calidad: ([a-záéíóúñ ]+)");
            Matcher mtcQuality = rgxQuality.matcher(qualityTag.text().toLowerCase());
            String quality;
            if (mtcQuality.find())
                quality = mtcQuality.group(1);
            else
                throw new UnexpectedStructureException("Can't parse qualityTag");

            result[i] = new VideoSiteLink(this, anchor.attr("href"), language, subtitles, quality);
        }

        return result;
    }

    private static class SearchResultEntry {
        private String permalink;
        private String image;
        private String title;
        private String meta;
        private String imdb_id;
    }

    private static class EpisodesAPIResult {
        private String permalink;
        private String thumbnail;
        private int season;
        private int episode;
        private Map<String, String> title;
    }

}
