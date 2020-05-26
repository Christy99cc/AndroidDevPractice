package bjfu.it.zhangsixuan.starbuzz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bjfu.it.zhangsixuan.starbuzz.R;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "starbuzz.db";
    private static final int DB_VERSION = 1;

    /*
     * 向父类构造函数传入数据库名称和版本
     */
    public StarbuzzDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
         * 创建STUFF表，并添加商品
         */
        String sql_stuff = "CREATE TABLE STUFF ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "DESCRIPTION TEXT," +
                "IMAGE_SOURCE_ID INTEGER," +
                "FAVORITE NUMERIC," +
                "PRICE NUMERIC, " +
                "CATEGORY INTEGER)";
        db.execSQL(sql_stuff);


        /*
         * 添加DRINK
         */
        insertStuff(db, "Latte",
                "Our dark, rich espresso balanced with steamed milk " +
                        "and a light layer of foam. " +
                        "A perfect milk-forward warm-up.",
                R.drawable.dk_latte_1, 25, 0);
        insertStuff(db, "Cappuccino",
                "Dark, rich espresso lies in wait under a smoothed " +
                        "and stretched layer of thick milk foam. " +
                        "An alchemy of barista artistry and craft.",
                R.drawable.dk_cappuccino_1, 27, 0);
        insertStuff(db, "Macchiato",
                "Freshly steamed milk with vanilla-flavored syrup marked with espresso " +
                        "and topped with a caramel drizzle for an oh-so-sweet finish.",
                R.drawable.dk_macchiato_1, 26, 0);


        /*
         * 添加FOOD
         */

        insertStuff(db, "Sandwich",
                "Sizzling applewood-smoked bacon, " +
                        "melty aged Gouda and a Parmesan frittata layered " +
                        "on an artisan roll for extra-smoky breakfast goodness.",
                R.drawable.fd_sandwich_1, 30, 1);
        insertStuff(db, "Oatmeal",
                "A blend of rolled and steel-cut oats with dried fruit, " +
                        "a nut medley and brown sugar as optional toppings. " +
                        "Hearty. Traditional. Classic.",
                R.drawable.fd_oatmeal_1, 32, 1);

        insertStuff(db, "Muffin",
                "This delicious muffin is dotted throughout with sweet, " +
                        "juicy blueberries and a hint of lemon and dusted on top " +
                        "with sugar for a delightfully crunchy texture.",
                R.drawable.fd_muffin_1, 25, 1);



        /*
         * 添加STORE
         */
        insertStuff(db, "VIAInstant",
                "Refreshing, revitalizing cool lime in our convenient, " +
                        "take-anywhere Starbucks VIA Instant Refreshers™ beverage packets.",
                R.drawable.st_viainstant_1, 50, 2);
        insertStuff(db, "VerismoPods",
                "Medium Roast — Unmistakable grapefruit and black currant notes." +
                        " Delicious served over ice.",
                R.drawable.st_verismopods_1, 52, 2);
        insertStuff(db, "WholeBean",
                "Named after the mist that casts a blue-tinged glow over " +
                        "Jamaican mountainsides, this coffee is a returning customer favorite, " +
                        "with layers of citrus and a hint of cocoa.",
                R.drawable.st_wholebean_1, 56, 2);


        /*
         * 增加CART表
         */
        String sql_cart = "CREATE TABLE CART ( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "STUFF_ID INTEGER," +
                "NUMBER INTEGER," +
                "FOREIGN KEY (STUFF_ID) REFERENCES parent(ID) ON DELETE CASCADE ON UPDATE CASCADE)";
        db.execSQL(sql_cart);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void insertStuff(SQLiteDatabase db,
                                    String name, String description, int resourceId,
                                    double price, int category) {
        ContentValues staffValues = new ContentValues();
        staffValues.put("NAME", name);
        staffValues.put("DESCRIPTION", description);
        staffValues.put("IMAGE_SOURCE_ID", resourceId);
        staffValues.put("FAVORITE", 0);
        staffValues.put("PRICE", price);
        staffValues.put("CATEGORY", category);

        long result = db.insert("STUFF", null, staffValues);
        Log.d("sqlite", "insert" + name + ",_id" + result);
    }

    private static void insertStuffToCart(SQLiteDatabase db, int stuffId,
                                          int number) {
        ContentValues staffValues = new ContentValues();
        staffValues.put("STUFF_ID", stuffId);
        staffValues.put("NUMBER", number);

        long result = db.insert("CART", null, staffValues);
        Log.d("sqlite", "insert" + stuffId + ",ID" + result);
    }
}
