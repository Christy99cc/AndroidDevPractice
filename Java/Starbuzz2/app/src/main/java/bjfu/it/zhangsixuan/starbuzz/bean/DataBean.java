package bjfu.it.zhangsixuan.starbuzz.bean;

public class DataBean {
    private int imageRes;
    private int id;
    private String name;

    public DataBean(int id, String name, int imageRes) {
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

