package bartibart.downloader.bandcamp.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import bartibart.downloader.bandcamp.model.AlbumInfo;
import bartibart.downloader.bandcamp.model.TrackInfo;
import bartibart.downloader.bandcamp.utils.Downloader;
import bartibart.downloader.bandcamp.utils.Parser;

public class BandcampDownloader {

    private static final int DOWNLOAD_ALBUM = 0;
    private static final int DOWNLOAD_TRACK = 1;

    private static String downloadDirectory = null;
    private static String url = null;
    private static Integer whatToDownload = null;

    public static void main(String[] args) {
        // load default properties
        loadProperties();

        // parse args
        if (args.length == 1) {
            setURL(args[0]);
        } else if (args.length == 2) {
            if (args[0].startsWith("-dir=")) {
                downloadDirectory = args[0].substring("-dir=".length());
                setURL(args[1]);
            }
        }
        if (!isEverythingSet()) {
            System.out.println(help());
            System.exit(0);
        }

        // download
        switch (whatToDownload) {
            case DOWNLOAD_ALBUM:
                downloadAlbum();
                break;
            case DOWNLOAD_TRACK:
                downloadTrack();
                break;
        }

        // TODO tag
    }

    private static boolean isEverythingSet() {
        return downloadDirectory != null && url != null && whatToDownload != null;
    }

    private static void loadProperties() {
        Properties properties = AppConfig.getProperties();
        downloadDirectory = properties.getProperty(AppConfig.PROPERTY_DEFAULT_DIRECTORY);
    }

    private static String help() {
        return "Usage:\n"
             + "  BandcampDownloader [-dir=<download directory>] <URL>\n"
             + "\n"
             + "URL has to be one of:\n"
             + "  https://<artist>.bandcamp.com/album/<album>\n"
             + "  https://<artist>.bandcamp.com/track/<track>\n"
             + "  <artist>.bandcamp.com/album/<album>\n"
             + "  <artist>.bandcamp.com/track/<track>\n"
             + "\n"
             + "Default download directory can be set via changing \"" + AppConfig.PROPERTY_DEFAULT_DIRECTORY + "\" property in \"" + AppConfig.CONFIG_FILE + "\" file.\n"
             + "Alternatively, via command:\n"
             + "  java -cp <jar name> bartibart.downloader.bandcamp.app.AppConfig -edit " + AppConfig.PROPERTY_DEFAULT_DIRECTORY + "=<new value>";
    }

    private static void setURL(String rawUrl) {
        // check if it is an album url
        if (rawUrl.matches("https://[^/]+\\.bandcamp\\.com/album/[^/]+/?")) {
            url = rawUrl;
            whatToDownload = DOWNLOAD_ALBUM;
        } else if (rawUrl.matches("[^/]+\\.bandcamp\\.com/album/[^/]+/?")) {   // no protocole
            url = "https://" + rawUrl;
            whatToDownload = DOWNLOAD_ALBUM;
        }
        // check if it is a track url
        if (rawUrl.matches("https://[^/]+\\.bandcamp\\.com/track/[^/]+/?")) {
            url = rawUrl;
            whatToDownload = DOWNLOAD_TRACK;
        } else if (rawUrl.matches("[^/]+\\.bandcamp\\.com/track/[^/]+/?")) {   // no protocole
            url = "https://" + rawUrl;
            whatToDownload = DOWNLOAD_TRACK;
        }
    }

    private static void downloadAlbum() {
        try {
            AlbumInfo album = Parser.getAlbumInfo(new URL(url));
            if (album == null) {
                System.out.println("Could not load album info!");
                System.exit(1);
            } else {
                System.out.println("ALBUM: " + album.getAlbumNameFormatted()
                    + " (" + album.getNumberOfTracks() + " tracks)");
                Downloader.downloadAlbum(album, downloadDirectory);
                System.out.println("All done!");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Invalid URL!");
            System.exit(1);
        }
    }

    private static void downloadTrack() {
        try {
            TrackInfo track = Parser.getTrackInfo(new URL(url));
            if (track == null) {
                System.out.println("Could not load track info!");
                System.exit(1);
            } else {
                System.out.println("TRACK: " + track.getTrackNameFormatted());
                Downloader.downloadTrack(track, downloadDirectory);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Invalid URL!");
            System.exit(1);
        }
    }

}
