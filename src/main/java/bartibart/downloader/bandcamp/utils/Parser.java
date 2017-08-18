package bartibart.downloader.bandcamp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import bartibart.downloader.bandcamp.model.AlbumInfo;
import bartibart.downloader.bandcamp.model.TrackInfo;

public class Parser {

    /*
    public static final String IFRAME_FORMAT = "<iframe src=\"https://bandcamp.com/EmbeddedPlayer/album=?/\"></iframe>";
    public static final String PLAYER_DATA_FORMAT = "var playerdata =";

    private static String setAlbumId(String albumId) {
        return IFRAME_FORMAT.replaceFirst("\\?", albumId);
    }

    private static JsonObject getAlbumInfoAsJson(String albumId) throws IOException {
        String html = setAlbumId(albumId);
        Document doc = Jsoup.parse(html);
        Element iframeElement = doc.select("iframe").first();
        String iframeSrc = iframeElement.attr("src");

        if(iframeSrc != null) {
            Document iframeContentDoc = Jsoup.connect(iframeSrc).get();
            Elements scriptElements = iframeContentDoc.getElementsByTag("script");
            for (Element element : scriptElements) {
                for (DataNode node : element.dataNodes()) {
                    String data = node.getWholeData();
                    if (data.contains(PLAYER_DATA_FORMAT)) {
                        String[] lines = data.split("\n");
                        for (String line : lines) {
                            if (line.contains(PLAYER_DATA_FORMAT)) {
                                JsonObject jsonObject = new JsonParser().parse(
                                        (line = line.trim())                                        // line = "PLAYER_DATA_FORMAT {json object};"
                                        .substring(PLAYER_DATA_FORMAT.length(), line.length() - 1)  // line = "{json object}"
                                    ).getAsJsonObject();
                                return jsonObject;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private static String test(URL url) throws IOException {
//        URLConnection connection = url.openConnection();
//        connection.connect();
        try (InputStream in = url.openStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                boolean foundStart = false;
                StringBuilder json = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    int indexStart = 0;
                    if (!foundStart) {
                        if ((indexStart = line.indexOf(HTML_PARSE_START)) >= 0) {
                            foundStart = true;
                            indexStart += HTML_PARSE_START.length() - 1;
                        }
                    }
                    if (foundStart) {
                        int indexStop = line.indexOf(HTML_PARSE_STOP);
                        if (indexStop < 0) {
                            json.append(line.substring(indexStart)).append("\n");
                        } else {
                            indexStop += HTML_PARSE_STOP.length() - 1;
                            json.append(line.substring(indexStart, indexStop));
                            break;
                        }
                    }
                }
                return json.toString();
            }
        }
    }

    private static String getAlbumInfoAsJson(URL url) throws IOException {
        Document doc = Jsoup.parse(url, 20000);
        Elements scripts = doc.getElementsByTag("script");
        for (Element script : scripts) {
            String code = script.html();
            int start = code.indexOf("var TralbumData = ");
            if (start >= 0) {
                code = code.substring(start);
                code = code.substring(0, code.indexOf("};"));
                return code;
            }
        }
        return null;
    }

    private static TrackInfo parseTrackInfo1(JsonObject trackInfoAsJson) {
        final TrackInfo trackInfo = new TrackInfo();
        trackInfo.setTrackNumber(trackInfoAsJson.get("tracknum").getAsInt() + 1);
        trackInfo.setTrackName(trackInfoAsJson.get("title").getAsString());
        trackInfoAsJson.get("file").getAsJsonObject().entrySet().stream().forEach(e -> {
            //trackInfo.addDownloadLink(e.getKey(), e.getValue().getAsString());
        });
        return trackInfo;
    }

    private static AlbumInfo parseAlbumInfo1(JsonObject albumInfoAsJson) {
        final AlbumInfo albumInfo = new AlbumInfo();
        albumInfo.setArtistName(albumInfoAsJson.get("artist").getAsString());
        albumInfo.setAlbumName(albumInfoAsJson.get("album").getAsString());
        albumInfo.setCoverUrl(albumInfoAsJson.get("album_art_lg").getAsString());
        albumInfo.setReleaseDate(albumInfoAsJson.get("publish_date").getAsString());
        JsonArray tracks = albumInfoAsJson.get("tracks").getAsJsonArray();
        albumInfo.setNumberOfTracks(tracks.size());
        for (JsonElement track : tracks) {
            //albumInfo.addTrack(parseTrackInfo(track.getAsJsonObject()));
        }
        return albumInfo;
    }

    public static AlbumInfo getAlbumInfo(String albumId) {
        try {
            JsonObject albumInfoAsJson = getAlbumInfoAsJson(albumId);
            return parseAlbumInfo(albumInfoAsJson);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    */

    private static final String HTML_PARSE_START = "var TralbumData = {";
    private static final String HTML_PARSE_STOP = "};";

    private static final String JSON_KEY_INFO = "current";
    private static final String JSON_KEY_ARTIST = "artist";
    private static final String JSON_KEY_DATE = "album_release_date";
    private static final String JSON_KEY_TRACKS = "trackinfo";
    private static final String JSON_KEY_COVER = "art_id"; // TODO
    private static final String[] JSON_KEYS = {JSON_KEY_INFO, JSON_KEY_ARTIST, JSON_KEY_DATE, JSON_KEY_TRACKS, JSON_KEY_COVER};

    private static JsonObject getJson(URL url) throws IOException {
        try (InputStream in = url.openStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                boolean foundStart = false;
                StringBuilder json = new StringBuilder("{\n");
                while ((line = reader.readLine()) != null) {
                    if (!foundStart) {
                        foundStart = line.indexOf(HTML_PARSE_START) >= 0;
                        continue;
                    }
                    if (foundStart) {
                        if (line.indexOf(HTML_PARSE_STOP) >= 0) {
                            json.append("\"fuck\": \"you\"");
                            json.append("\n}");
                            break;
                        }
                        line = line.trim();
                        for (String key : JSON_KEYS) {
                            if (line.startsWith(key)) {
                                json.append(line);
                                json.append(line.endsWith(",") ? "\n" : ",\n");
                            }
                        }
                    }
                }
                return new JsonParser().parse(json.toString()).getAsJsonObject();
            }
        }
    }

    private static URL parseURL(String url) throws MalformedURLException {
        if (url.startsWith("//")) {
            url = "http:" + url;
        }
        return new URL(url);
    }

    private static String parseYear(String date) {
        if (date == null) {
            return null;
        }
        String year = "";
        for (int i = 0; i < date.length(); ++i) {
            char c = date.charAt(i);
            if (c >= '0' && c <= '9') {
                year += c;
            } else {
                year = "";
            }
            if (year.length() == 4) {
                return year;
            }
        }
        return date;
    }

    private static TrackInfo parseTrackInfo(JsonObject json) {
        TrackInfo track = new TrackInfo();
        track.setArtistName(json.get(JSON_KEY_ARTIST).getAsString());
        String title = json.get(JSON_KEY_INFO).getAsJsonObject().get("title").getAsString();
        track.setTrackName(title.startsWith(track.getArtistName() + " - ") ? title.substring((track.getArtistName() + " - ").length()) : title);
        track.setFromAlbum(false);
        JsonArray tracks = json.get(JSON_KEY_TRACKS).getAsJsonArray();
        if (tracks.size() > 0) {
            JsonElement elem = tracks.get(0).getAsJsonObject().get("file");
            if (!elem.isJsonNull()) {
                JsonObject links = elem.getAsJsonObject();
                for (Entry<String, JsonElement> link : links.entrySet()) {
                    try {
                        URL url = parseURL(link.getValue().getAsString());
                        track.addDownloadLink(link.getKey(), url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return track;
    }

    private static AlbumInfo parseAlbumInfo(JsonObject json) {
        AlbumInfo album = new AlbumInfo();
        album.setArtistName(json.get(JSON_KEY_ARTIST).getAsString());
        album.setAlbumName(json.get(JSON_KEY_INFO).getAsJsonObject().get("title").getAsString());
        album.setReleaseDate(parseYear(json.get(JSON_KEY_DATE).isJsonNull() ? null : json.get(JSON_KEY_DATE).getAsString()));
        // TODO cover
        JsonArray tracks = json.get(JSON_KEY_TRACKS).getAsJsonArray();
        album.setNumberOfTracks(tracks.size());
        for (JsonElement elem : tracks) {
            TrackInfo track = new TrackInfo();
            JsonObject obj = elem.getAsJsonObject();
            track.setArtistName(album.getArtistName());
            track.setTrackName(obj.get("title").getAsString());
            track.setTrackNumber(obj.get("track_num").getAsInt());
            track.setFromAlbum(true);
            if (!obj.get("file").isJsonNull()) {
                JsonObject links = obj.get("file").getAsJsonObject();
                for (Entry<String, JsonElement> link : links.entrySet()) {
                    try {
                        URL url = parseURL(link.getValue().getAsString());
                        track.addDownloadLink(link.getKey(), url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
            album.addTrack(track);
        }
        return album;
    }

    public static TrackInfo getTrackInfo(URL url) {
        try {
            JsonObject json = getJson(url);
            return parseTrackInfo(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AlbumInfo getAlbumInfo(URL url) {
        try {
            JsonObject json = getJson(url);
            return parseAlbumInfo(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
