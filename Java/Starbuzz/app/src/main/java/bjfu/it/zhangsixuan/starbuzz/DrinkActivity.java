package bjfu.it.zhangsixuan.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKID = "drinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkId = getIntent().getIntExtra(EXTRA_DRINKID, 0);

        /*
         * 先实例化helper，再获取数据库引用
         */
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        /*
         * try with resource 自动关闭数据库
         */
        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            Cursor cursor = db.query("DRINK",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_SOURCE_ID", "FAVORITE"},
                    "_id=?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                // 显示name
                TextView name = findViewById(R.id.name);
                name.setText(nameText);

                // 显示description
                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);

                // 显示photo
                ImageView photo = findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                // 显示收藏
                CheckBox favorite = findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();


        } catch (SQLiteException e) {
            Log.d("sqlite", e.getMessage());
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    public void onfavoriteClicked(View view) {
        CheckBox favorite = (CheckBox) view;
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("FAVORITE", favorite.isChecked());

        // 从intent中获取drinkId
        int drindId = getIntent().getIntExtra(EXTRA_DRINKID, 0);

        // 获得可写数据库引用
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try (SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase()) {
            // 根据主键更新
            int row = db.update("DRINK", drinkValues, "_id=?",
                    new String[]{Integer.toString(drindId)});
            Log.d("sqlite", "update row " + row);
        } catch (SQLiteException e) {
            Log.d("sqlite", e.getMessage());
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}
