package bjfu.it.zhangsixuan.starbuzz;

public class Drink {

    private String name;
    private String description;
    private int imageResourceId;

    private Drink(String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    //定义包含三种咖啡的常量数组drinks
    public static final Drink[] drinks = {
            new Drink("Latte","A couple of espresso shots with sreamed milk", R.drawable.latte),
            new Drink("Cappuccino", "Espresso, hot milk, and a steamed milk foam", R.drawable.cappuccino),
            new Drink("Filter", "Highest quality beans roasted and brewed fresh", R.drawable.filter)
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResource() {
        return imageResourceId;
    }

    public void setImageResource(int imageResource) {
        this.imageResourceId = imageResource;
    }


    @Override
    public String toString() {
        return this.name;
    }


}
