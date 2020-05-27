package bjfu.it.zhangsixuan.starbuzz.bean;

public class BannerBean {
    private int imageRes;
    private int id;
    private String name;

    public BannerBean(int id, String name, int imageRes) {
        this.imageRes = imageRes;
        this.id = id;
        this.name = name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

