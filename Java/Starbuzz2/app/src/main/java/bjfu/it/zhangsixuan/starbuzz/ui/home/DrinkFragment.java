package bjfu.it.zhangsixuan.starbuzz.ui.home;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

public class DrinkFragment extends Fragment {

    public static final String EXTRA_DRINKID = "drinkId";
    private int drinkId;
    private CheckBox cb_favorite;
    private Cursor cursor;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //获取Bundle
        Bundle bundle = getArguments();
        assert bundle != null;
        drinkId = bundle.getInt(EXTRA_DRINKID);
        drinkId = bundle.getInt(EXTRA_DRINKID, 0);

        View root = inflater.inflate(R.layout.fragment_drink, container, false);

        cb_favorite = root.findViewById(R.id.favorite);

        /*
         * 先实例化helper，再获取数据库引用
         */
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        /*
         * try with resource 自动关闭数据库
         */
        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            Log.d("debug", "drinkId:" + drinkId);
            cursor = db.query("DRINK",
                    new String[]{"NAME",
                            "DESCRIPTION",
                            "IMAGE_SOURCE_ID",
                            "FAVORITE"
                    },
                    "_id=?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                // 显示name
                TextView name = root.findViewById(R.id.name);
                name.setText(nameText);

                // 显示description
                TextView description = root.findViewById(R.id.description);
                description.setText(descriptionText);

                // 显示photo
                ImageView photo = root.findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                // 显示收藏
                cb_favorite.setChecked(isFavorite);
            }
            cursor.close();


        } catch (SQLiteException e) {
            Log.d("sqlite", Objects.requireNonNull(e.getMessage()));
            Toast toast = Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        //获取Bundle
        Bundle bundle = getArguments();
        drinkId = bundle.getInt(EXTRA_DRINKID, 0);


        cb_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues drinkValues = new ContentValues();
                drinkValues.put("FAVORITE", cb_favorite.isChecked());

                // 获得可写数据库引用
                SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
                try (SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase()) {
                    // 根据主键更新
                    int row = db.update("DRINK", drinkValues, "_id=?",
                            new String[]{Integer.toString(drinkId)});
                    Log.d("sqlite", "update row " + row);
                } catch (SQLiteException e) {
                    Log.d("sqlite", e.getMessage());
                    Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
