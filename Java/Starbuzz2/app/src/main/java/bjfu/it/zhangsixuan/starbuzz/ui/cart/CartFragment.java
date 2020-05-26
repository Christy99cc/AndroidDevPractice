package bjfu.it.zhangsixuan.starbuzz.ui.cart;

import android.annotation.SuppressLint;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

import static android.widget.Toast.*;
import static bjfu.it.zhangsixuan.starbuzz.MainActivity.CART_TABLE;
import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;

public class CartFragment extends Fragment {

    private List<Map<String, Object>> mData;

    private Cursor cursor;
    private Cursor stuffCursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 通过CART数据库的数据，来决定加载哪一个fragment

        View root = null;

        mData = getData();

        // 通过list有无数据，决定显示which one
        if (mData.size() == 0) {
            root = inflater.inflate(R.layout.fragment_vacant_cart, container, false);
        } else {
            root = inflater.inflate(R.layout.fragment_cart, container, false);
            RecyclerView rv_cart = root.findViewById(R.id.rv_cart);
            RVCartAdapter rvCartAdapter = new RVCartAdapter(mData);
            rv_cart.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_cart.setAdapter(rvCartAdapter);

            TextView tv_total_price = root.findViewById(R.id.total_price);
            tv_total_price.setText(String.valueOf(getTotalPrice()));
        }


        return root;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // map数据准备
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            // 全部取出来
            cursor = db.query(CART_TABLE, new String[]{"ID", "STUFF_ID", "NUMBER"}, null,
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
                stuffCursor = db.query(STUFF_TABLE,
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
            makeText(getActivity(), "database unavailable", LENGTH_SHORT).show();
        }
        return list;
    }


    public static class RVCartAdapter extends RecyclerView.Adapter {

        List<Map<String, Object>> list;

        public RVCartAdapter(List<Map<String, Object>> mData) {
            this.list = mData;
        }

        @NonNull
        @Override
        public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.cart_item_layout, null);
            return new RViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            // 注意强制转换，一定要是显式的
            ((RViewHolder) holder).iv_cart_img.setImageResource((int) list.get(position).get("stuffImgId"));
            ((RViewHolder) holder).tv_item_name.setText((String) list.get(position).get("stuffName"));
            ((RViewHolder) holder).tv_item_price.setText(String.valueOf(list.get(position).get("stuffPrice")));
            ((RViewHolder) holder).tv_item_number.setText(String.valueOf(list.get(position).get("stuffNumber")));
            // +\- 采用默认即可

            // 点击事件
            // +
            ((RViewHolder) holder).iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("debug", "点击了position 的+号");
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

    public static class RViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_cart_img;  // 图片
        TextView tv_item_name;  // 名字
        TextView tv_item_price; // 价格
        ImageView iv_reduce;
        ImageView iv_add;
        TextView tv_item_number; // 数量

        RViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_cart_img = itemView.findViewById(R.id.iv_cart_item);
            tv_item_name = itemView.findViewById(R.id.tv_cart_item);
            tv_item_price = itemView.findViewById(R.id.tv_cart_item_price);
            iv_reduce = itemView.findViewById(R.id.iv_reduce);
            iv_add = itemView.findViewById(R.id.iv_add);
            tv_item_number = itemView.findViewById(R.id.tv_amount);
        }
    }

    // 计算mData的总价
    private double getTotalPrice(){
        double tol = 0;
        for (Map<String, Object> mDatum : mData) {
            double perPrice = (double) mDatum.get("stuffPrice");
            int number = (int) mDatum.get("stuffNumber");
            tol += perPrice * number;
        }
        return tol;
    }
}
