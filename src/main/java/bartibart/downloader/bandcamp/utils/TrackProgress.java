package bartibart.downloader.bandcamp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class TrackProgress {

    private String name;
    private int total;
    private int downloaded;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public BigDecimal getPercentage() {
        if (total <= 0) {
            return null;
        }
        return new BigDecimal(downloaded)
                .multiply(new BigDecimal(100))
                .divide(new BigDecimal(total), 2, RoundingMode.HALF_UP);
    }

    public boolean isDone() {
        if (total <= 0) {
            return true;
        }
        return total == downloaded;
    }

    public abstract void onStart();
    public abstract void onNoUrl();
    public abstract void onUpdate(int downloadedPortion);
    public abstract void onSuccess();
    public abstract void onFail();
    public abstract void onFinish();

}
