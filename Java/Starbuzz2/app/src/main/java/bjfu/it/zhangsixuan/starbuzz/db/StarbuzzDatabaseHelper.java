package bjfu.it.zhangsixuan.starbuzz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bjfu.it.zhangsixuan.starbuzz.R;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "starbuzz.db";
    private static final int DB_VERSION = 2;

    /*
     * 向父类构造函数传入数据库名称和版本
     */
    public StarbuzzDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
         * 创建DRINK表，并添加饮品
         */
        String sql_dk = "CREATE TABLE DRINK ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "DESCRIPTION TEXT," +
                "IMAGE_SOURCE_ID INTEGER," +
                "FAVORITE NUMERIC," +
                "PRICE NUMERIC)";
        db.execSQL(sql_dk);
        insertStuff(db, "DRINK","Latte",
                "Our dark, rich espresso balanced with steamed milk " +
                        "and a light layer of foam. " +
                        "A perfect milk-forward warm-up.",
                R.drawable.dk_latte_1, 25);
        insertStuff(db, "DRINK","Cappuccino",
                "Dark, rich espresso lies in wait under a smoothed " +
                        "and stretched layer of thick milk foam. " +
                        "An alchemy of barista artistry and craft.",
                R.drawable.dk_cappuccino_1, 27);
        insertStuff(db, "DRINK","Macchiato",
                "Freshly steamed milk with vanilla-flavored syrup marked with espresso " +
                        "and topped with a caramel drizzle for an oh-so-sweet finish.",
                R.drawable.dk_macchiato_1, 26);


        /*
         * 创建FOOD表，并添加食品
         */
        String sql_fd = "CREATE TABLE FOOD ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "DESCRIPTION TEXT," +
                "IMAGE_SOURCE_ID INTEGER," +
                "FAVORITE NUMERIC," +
                "PRICE NUMERIC)";
        db.execSQL(sql_fd);

        insertStuff(db, "FOOD","Sandwich",
                "Sizzling applewood-smoked bacon, " +
                        "melty aged Gouda and a Parmesan frittata layered " +
                        "on an artisan roll for extra-smoky breakfast goodness.",
                R.drawable.fd_sandwich_1, 30);
        insertStuff(db, "FOOD","Oatmeal",
                "A blend of rolled and steel-cut oats with dried fruit, " +
                        "a nut medley and brown sugar as optional toppings. " +
                        "Hearty. Traditional. Classic.",
                R.drawable.fd_oatmeal_1, 32);

        insertStuff(db, "FOOD","Muffin",
                "This delicious muffin is dotted throughout with sweet, " +
                        "juicy blueberries and a hint of lemon and dusted on top " +
                        "with sugar for a delightfully crunchy texture.",
                R.drawable.fd_muffin_1, 25);



        /*
         * 创建STORE表，并添加商品
         */
        String sql_st = "CREATE TABLE STORE ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "DESCRIPTION TEXT," +
                "IMAGE_SOURCE_ID INTEGER," +
                "FAVORITE NUMERIC," +
                "PRICE NUMERIC)";
        db.execSQL(sql_st);
        insertStuff(db, "STORE","VIAInstant",
                "Refreshing, revitalizing cool lime in our convenient, " +
                        "take-anywhere Starbucks VIA Instant Refreshers™ beverage packets.",
                R.drawable.st_viainstant_1, 50);
        insertStuff(db, "STORE","VerismoPods",
                "Medium Roast — Unmistakable grapefruit and black currant notes." +
                        " Delicious served over ice.", R.drawable.st_verismopods_1, 52);
        insertStuff(db, "STORE","WholeBean",
                "Named after the mist that casts a blue-tinged glow over " +
                        "Jamaican mountainsides, this coffee is a returning customer favorite, " +
                        "with layers of citrus and a hint of cocoa.",
                R.drawable.st_wholebean_1, 56);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 数据库升级到2，添加购物车表
        if (oldVersion <= 1){
            /*
             * 创建CART表，并添加商品
             */
            String sql_cart = "CREATE TABLE CART ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT," +
                    "IMAGE_SOURCE_ID INTEGER," +
                    "PRICE NUMERIC, " +
                    "NUMBER INTEGER)";
            db.execSQL(sql_cart);
        }

    }

    private static void insertStuff(SQLiteDatabase db, String tableName,
                                    String name, String description, int resourceId, double price) {
        ContentValues staffValues = new ContentValues();
        staffValues.put("NAME", name);
        staffValues.put("DESCRIPTION", description);
        staffValues.put("IMAGE_SOURCE_ID", resourceId);
        staffValues.put("PRICE", price);
        long result = db.insert(tableName, null, staffValues);
        Log.d("sqlite", "insert" + name + ",_id" + result);
    }
}
