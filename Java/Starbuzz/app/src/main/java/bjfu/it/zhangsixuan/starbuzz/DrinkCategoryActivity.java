package bjfu.it.zhangsixuan.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DrinkCategoryActivity extends AppCompatActivity {

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_category);
        ListView listDrinks = findViewById(R.id.list_drinks);
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {
            /*
             * selection 的位置是null
             */
            cursor = db.query("DRINK", new String[]{"_id", "NAME"}, null,
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                    this,
                    // 内置布局，每行显示一个文本框
                    android.R.layout.simple_list_item_1,
                    // 数据源
                    cursor,
                    // 显示name
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0
            );

            // 指定适配器
            listDrinks.setAdapter(listAdapter);

        } catch (SQLiteException e) {
            Log.e("sqlite", e.getMessage());
            Toast.makeText(this, "database unavailable", Toast.LENGTH_SHORT).show();
        }

        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listDrinks,
                                    View view,
                                    int position,
                                    long id) {
                Intent intent = new Intent(DrinkCategoryActivity.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int) id);
                startActivity(intent);
            }
        };

        // 设置监听器
        listDrinks.setOnItemClickListener(itemClickListener);
//        ListView listView = findViewById(R.id.list_drinks);
//        listView.setOnItemClickListener(itemClickListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
    }
}
