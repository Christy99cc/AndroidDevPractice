package bjfu.it.zhangsixuan.starbuzz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bjfu.it.zhangsixuan.starbuzz.R;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "starbuzz.db";
    private static final int DB_VERSION =1;

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
                "Espresso and steamed milk", R.drawable.dk_latte, 25);
        insertStuff(db, "DRINK","Cappuccino",
                "Espresso, hot milk and steamed-milk foam", R.drawable.dk_cappuccino, 27);
        insertStuff(db, "DRINK","Filter",
                "Our best drip coffee", R.drawable.dk_filter, 22);

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
        insertStuff(db, "FOOD","Latte",
                "Espresso and steamed milk", R.drawable.dk_latte, 25);
        insertStuff(db, "FOOD","Cappuccino",
                "Espresso, hot milk and steamed-milk foam", R.drawable.dk_cappuccino, 27);
        insertStuff(db, "FOOD","Filter",
                "Our best drip coffee", R.drawable.dk_filter, 22);


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
        insertStuff(db, "STORE","Latte",
                "Espresso and steamed milk", R.drawable.dk_latte, 25);
        insertStuff(db, "STORE","Cappuccino",
                "Espresso, hot milk and steamed-milk foam", R.drawable.dk_cappuccino, 27);
        insertStuff(db, "STORE","Filter",
                "Our best drip coffee", R.drawable.dk_filter, 22);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        

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
