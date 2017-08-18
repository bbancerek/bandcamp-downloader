package bartibart.downloader.bandcamp.utils;

public class TrackProgressPrintToSTDOUT extends TrackProgress {

    private final int trackNameMaxLenght = 40;
    private final int step = 25;
    private int lastViewedPercentage = -step;

    @Override
    public String getName() {
        String name = super.getName();
        return name.length() > trackNameMaxLenght ?
            name.substring(0, trackNameMaxLenght - 3) + "..." :
            name;
    }

    @Override
    public void onStart() {
        String info = "Downloading \""
            + String.format("%1$-" + (trackNameMaxLenght + 2) + "s", getName() + "\":") // pad right
            + " ";
        System.out.print(info);
    }

    @Override
    public void onNoUrl() {
        System.out.print("[NO URL]");
    }

    @Override
    public void onUpdate(int downloadedPortion) {
        setDownloaded(getDownloaded() + downloadedPortion);
        if (getPercentage().intValue() >= lastViewedPercentage + step) {
            lastViewedPercentage += step;
            System.out.print(lastViewedPercentage + "%... ");
        }
    }

    @Override
    public void onSuccess() {
        System.out.print("[SUCCESS]");
    }

    @Override
    public void onFail() {
        System.out.print("[FAIL]");
    }

    @Override
    public void onFinish() {
        System.out.println();
    }

}
