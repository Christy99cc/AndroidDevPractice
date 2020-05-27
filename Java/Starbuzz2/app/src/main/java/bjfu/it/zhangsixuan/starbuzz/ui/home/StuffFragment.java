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
import android.widget.Button;
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
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;

public class StuffFragment extends Fragment {

    public static final String EXTRA_STUFFID = "stuffId";

    private int stuffId;

    private CheckBox cb_favorite;

    private TextView tv_price;

    private Button btn_add_to_cart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //获取Bundle
        Bundle bundle = getArguments();
        assert bundle != null;
        stuffId = bundle.getInt(EXTRA_STUFFID);

        View root = inflater.inflate(R.layout.fragment_stuff, container, false);
        cb_favorite = root.findViewById(R.id.favorite);
        tv_price = root.findViewById(R.id.price);
        btn_add_to_cart = root.findViewById(R.id.btn_add_to_cart);

        /*
         * 先实例化helper，再获取数据库引用
         */
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        /*
         * try with resource 自动关闭数据库
         */
        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            Log.d("debug", "stuffId:" + stuffId);
            Cursor cursor = db.query(STUFF_TABLE,
                    new String[]{"NAME",
                            "DESCRIPTION",
                            "IMAGE_SOURCE_ID",
                            "FAVORITE",
                            "PRICE"
                    },
                    "_id=?",
                    new String[]{Integer.toString(stuffId)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);
                double price = cursor.getDouble(4);

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

                // 显示价格
                Log.d("debug", "price:" + price);
                tv_price.setText("" + price);

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

        stuffId = bundle.getInt(EXTRA_STUFFID);

        // 点击收藏 或者取消收藏
        cb_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues drinkValues = new ContentValues();
                drinkValues.put("FAVORITE", cb_favorite.isChecked());

                // 获得可写数据库引用
                SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
                try (SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase()) {
                    // 根据主键更新
                    int row = db.update(STUFF_TABLE, drinkValues, "_id=?",
                            new String[]{Integer.toString(stuffId)});
                    Log.d("sqlite", "update row " + row);
                } catch (SQLiteException e) {
                    Log.d("sqlite", e.getMessage());
                    Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 点击加入购物车按钮
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将现在的商品加入购物车
                Utils.addOneToCart(stuffId, getActivity());
            }
        });
    }
}
