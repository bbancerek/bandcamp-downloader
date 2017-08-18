package bartibart.downloader.bandcamp.model;

import java.util.ArrayList;
import java.util.List;

import bartibart.downloader.bandcamp.utils.StringFormatter;

public class AlbumInfo {

    private String artistName;
    private String albumName;
    private int numberOfTracks;
    private String coverUrl;
    private String releaseDate;
    private List<TrackInfo> tracks = new ArrayList<>();

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<TrackInfo> getTracks() {
        return tracks;
    }

    public void addTrack(TrackInfo track) {
        tracks.add(track);
    }

    public String getAlbumNameFormatted() {
        return StringFormatter.removeForbiddenChars(artistName) + " - " + StringFormatter.removeForbiddenChars(albumName);
    }

}
