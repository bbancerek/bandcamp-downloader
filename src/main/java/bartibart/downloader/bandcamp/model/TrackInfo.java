package bartibart.downloader.bandcamp.model;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import bartibart.downloader.bandcamp.utils.StringFormatter;

public class TrackInfo {

    private int trackNumber;
    private String artistName;
    private String trackName;
    private Map<String, URL> downloadLinks = new HashMap<>();
    private boolean fromAlbum;

    public static final String DEFAULT_DOWNLOAD_FORMAT = "mp3-128";


    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public boolean isFromAlbum() {
        return fromAlbum;
    }

    public void setFromAlbum(boolean fromAlbum) {
        this.fromAlbum = fromAlbum;
    }

    public URL getDownloadLink() {
        URL url = getDownloadLink(DEFAULT_DOWNLOAD_FORMAT);
        if (url == null && !downloadLinks.isEmpty()) {
            url = downloadLinks.entrySet().iterator().next().getValue();
        }   
        return url;
    }

    public URL getDownloadLink(String format) {
        return downloadLinks.get(format);
    }

    public void addDownloadLink(String format, URL url) {
        downloadLinks.put(format, url);
    }

    public boolean hasDownloadLink() {
        return !downloadLinks.isEmpty();
    }

    public String getTrackNameFormatted() {
        String name = StringFormatter.removeForbiddenChars(trackName) + ".mp3";
        if (fromAlbum) {
            return StringFormatter.formatTrackNumber(trackNumber) + ". " + name;
        } else {
            return StringFormatter.removeForbiddenChars(artistName) + " - " + name;
        }
    }

}
