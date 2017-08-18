package bartibart.downloader.bandcamp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import bartibart.downloader.bandcamp.model.AlbumInfo;
import bartibart.downloader.bandcamp.model.TrackInfo;

public class Downloader {

    /*
    public static void downloadTrackSilently(TrackInfo track, String path) {
        try {
            FileUtils.copyURLToFile(track.getDownloadLink(), new File(path + "/" + track.getTrackNameFormatted()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static final int PROTOCOL_HTTP  = 0;
//    private static final int PROTOCOL_HTTPS = 1;
//
//    private int getProtocol(String url) {
//        if (url.startsWith("https")) {
//            return PROTOCOL_HTTPS;
//        }
//        return PROTOCOL_HTTP;
//    }
    */

    private static final int BUFFER_SIZE = 1024;

    private static void downloadTrack(TrackInfo track, String path, TrackProgress progress) {
        try {
            // mark as started
            progress.setName(track.getTrackNameFormatted());
            progress.onStart();

            // check if url exists
            if (!track.hasDownloadLink()) {
                progress.onNoUrl();
                return;
            }

            // initialize connection
            URL url = track.getDownloadLink(); 
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() / 100 != 2) {
                progress.onFail();
                return;
            }
            progress.setTotal(connection.getContentLength());

            // read bytes and write to output file
            try (InputStream in = connection.getInputStream()) {
                String p = path + "/" + track.getTrackNameFormatted();
                try (FileOutputStream out = new FileOutputStream(new File(p))) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int n;
                    while ((n = in.read(buffer)) > 0) {
                        out.write(buffer, 0, n);
                        progress.onUpdate(n);
                    }
                }
            }

            // mark as success/fail
            if (progress.isDone()) {
                progress.onSuccess();
            } else {
                progress.onFail();
            }
        } catch (IOException e) {
            progress.onFail();
            e.printStackTrace();
        } finally {
            progress.onFinish();
        }
    }

    public static void downloadTrack(TrackInfo track, String path) {
        downloadTrack(track, path, new TrackProgressPrintToSTDOUT());
    }
    public static void downloadAlbum(AlbumInfo album, String path) {
        try {
            String p = path + "/" + album.getAlbumNameFormatted();
            Files.createDirectory(Paths.get(p));
            for (TrackInfo track : album.getTracks()) {
                downloadTrack(track, p, new TrackProgressPrintToSTDOUT());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
