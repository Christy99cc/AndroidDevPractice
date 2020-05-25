package bjfu.it.zhangsixuan.starbuzz.bean;

public class FavoriteBean {
    int stuffId;
    String stuffName;
    int stuffImageId;

    public FavoriteBean(int stuffId, String stuffName, int stuffImageId) {
        this.stuffId = stuffId;
        this.stuffName = stuffName;
        this.stuffImageId = stuffImageId;
    }

    public int getStuffId() {
        return stuffId;
    }

    public void setStuffId(int stuffId) {
        this.stuffId = stuffId;
    }

    public String getStuffName() {
        return stuffName;
    }

    public void setStuffName(String stuffName) {
        this.stuffName = stuffName;
    }

    public int getStuffImageId() {
        return stuffImageId;
    }

    public void setStuffImageId(int stuffImageId) {
        this.stuffImageId = stuffImageId;
    }
}
