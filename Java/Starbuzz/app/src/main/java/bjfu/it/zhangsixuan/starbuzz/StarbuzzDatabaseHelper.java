package bjfu.it.zhangsixuan.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "starbuzz.db";
    private static final int DB_VERSION = 3;

    /*
     * 向父类构造函数传入数据库名称和版本
     */
    public StarbuzzDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE DRINK ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "DESCRIPTION TEXT," +
                "IMAGE_SOURCE_ID INTEGER )";
        db.execSQL(sql);
        insertDrink(db, "Latte", "Espresso and steamed milk", R.drawable.latte);
        insertDrink(db, "Cappuccino", "Espresso, hot milk and steamed-milk foam", R.drawable.cappuccino);
        insertDrink(db, "Filter", "Our best drip coffee", R.drawable.filter);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 1) {
            String sql = "ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;";
            db.execSQL(sql);
        }

        if (oldVersion <= 2) {
            ContentValues latteDesc = new ContentValues();
            latteDesc.put("DESCRIPTION", "Tasty");
            db.update("DRINK", latteDesc, "NAME=?", new String[]{"Latte"});
//            db.update("DRINK", latteDesc, "_id=?", new String[]{Integer.toString(1)})
        }

    }

    private static void insertDrink(SQLiteDatabase db, String name, String description, int resourceId) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME", name);
        drinkValues.put("DESCRIPTION", description);
        drinkValues.put("IMAGE_SOURCE_ID", resourceId);
        long result = db.insert("DRINK", null, drinkValues);
        Log.d("sqlite", "insert" + name + ",_id" + result);
    }
}
