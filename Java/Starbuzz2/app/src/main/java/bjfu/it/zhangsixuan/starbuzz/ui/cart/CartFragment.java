package bjfu.it.zhangsixuan.starbuzz.ui.cart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.CART_TABLE;
import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;

public class CartFragment extends Fragment {

    private Cursor cursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 通过CART数据库的数据，来决定加载哪一个fragment

        View root = null;
        // map数据准备
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            // 全部取出来
            cursor = db.query(CART_TABLE, new String[]{"ID", "STUFF_ID","NUMBER"}, null,
                    null, null, null, null, null);

            if (!cursor.moveToFirst()){
                root=inflater.inflate(R.layout.fragment_vacant_cart, container, false);
            }else{
                root=inflater.inflate(R.layout.fragment_cart, container, false);
                // 添加
                // 需要的是：id, stuffId, stuffName(display), price, number
                while (cursor.moveToNext()) {
                    // 移动光标到下一行
                    Map<String, Object> map = new HashMap<String, Object>();

                }
            }

        } catch (SQLiteException e) {
            Log.e("sqlite", e.getMessage());
            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }

        return root;
    }
}
