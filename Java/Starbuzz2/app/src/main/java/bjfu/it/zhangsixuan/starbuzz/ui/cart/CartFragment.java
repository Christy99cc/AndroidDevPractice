package bjfu.it.zhangsixuan.starbuzz.ui.cart;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.adapter.RVCartAdapter;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;

import static android.widget.Toast.*;
import static bjfu.it.zhangsixuan.starbuzz.MainActivity.CART_TABLE;
import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;

public class CartFragment extends Fragment {

    private List<Map<String, Object>> mData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 通过CART数据库的数据，来决定加载哪一个fragment

        View root;

        // 获取购物车内的商品数据，即CART表的数据
        mData = getData(getActivity());

        // 通过list有无数据，决定显示which one
        if (mData.size() == 0) {
            root = inflater.inflate(R.layout.fragment_vacant_cart, container, false);
        } else {
            root = inflater.inflate(R.layout.fragment_cart, container, false);
            RecyclerView rv_cart = root.findViewById(R.id.rv_cart);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            RVCartAdapter rvCartAdapter = new RVCartAdapter(mData, getActivity(), transaction);
            rv_cart.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_cart.setAdapter(rvCartAdapter);

            TextView tv_total_price = root.findViewById(R.id.total_price);
            Utils.refreshTotalPrice(tv_total_price, mData);
        }


        return root;
    }

    public static List<Map<String, Object>> getData(Context context) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // map数据准备
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(context);

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            // 全部取出来
            Cursor cursor = db.query(CART_TABLE, new String[]{"ID", "STUFF_ID", "NUMBER"}, null,
                    null, null, null, null, null);

            while (cursor.moveToNext()) {
                // 添加
                // 需要的是：cartId, stuffId, stuffName(display), stuffPrice, stuffImgId, stuffNumber
                // 移动光标到下一行
                Map<String, Object> map = new HashMap<String, Object>();

                int cartId = cursor.getInt(cursor.getColumnIndex("ID"));
                int stuffId = cursor.getInt(cursor.getColumnIndex("STUFF_ID"));
                int stuffNumber = cursor.getInt(cursor.getColumnIndex("NUMBER"));
                map.put("cartId", cartId);
                map.put("stuffId", stuffId);
                map.put("stuffNumber", stuffNumber);


                // 到STUFF表中查询，根据stuffId查 stuffName(display), stuffPrice
                Cursor stuffCursor = db.query(STUFF_TABLE,
                        new String[]{"NAME", "PRICE", "IMAGE_SOURCE_ID"}, "_id=?",
                        new String[]{String.valueOf(stuffId)},
                        null, null, null, null);

                // 移动到第一条记录
                stuffCursor.moveToFirst();
                String stuffName = stuffCursor.getString(
                        stuffCursor.getColumnIndex("NAME"));
                double stuffPrice = stuffCursor.getDouble(
                        stuffCursor.getColumnIndex("PRICE"));
                int stuffImgId = stuffCursor.getInt(
                        stuffCursor.getColumnIndex("IMAGE_SOURCE_ID"));
                map.put("stuffName", stuffName);
                map.put("stuffPrice", stuffPrice);
                map.put("stuffImgId", stuffImgId);

                list.add(map);
            }

        } catch (
                SQLiteException e) {
            Log.e("sqlite", Objects.requireNonNull(e.getMessage()));
        }
        return list;
    }


}
