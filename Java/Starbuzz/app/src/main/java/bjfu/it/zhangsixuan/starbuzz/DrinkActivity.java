package bjfu.it.zhangsixuan.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
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
                    new String[]{"NAME", "DESCRIPTION","IMAGE_SOURCE_ID"},
                    "_id=?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null);

            if (cursor.moveToFirst()){
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);

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
            }
            cursor.close();


        } catch (SQLiteException e) {
            Log.e("sqlite", e.getMessage());
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }


    }
}
