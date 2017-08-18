package bartibart.downloader.bandcamp.utils;

public class StringFormatter {

    public static String removeForbiddenChars(String str) {
        return str
            .replace("<", "")
            .replace(">", "")
            .replace(":", "")
            .replace("\"", "")
            .replace("/", "")
            .replace("\\", "")
            .replace("|", "")
            .replace("?", "")
            .replace("*", "").trim();
    }

    public static String formatTrackNumber(int trackNumber) {
        if (trackNumber <= 0) {
            return "00";
        } else if (trackNumber < 10) {
            return "0" + trackNumber;
        } else {
            return "" + trackNumber;
        }
    }

}
