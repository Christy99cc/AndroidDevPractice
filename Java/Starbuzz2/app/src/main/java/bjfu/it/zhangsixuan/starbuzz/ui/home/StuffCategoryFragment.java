package bjfu.it.zhangsixuan.starbuzz.ui.home;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.CART_TABLE;
import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;

public class StuffCategoryFragment extends Fragment {
    private List<Map<String, Object>> mData;
    private Cursor cursor;

    static final String EXTRA_CATEGORY_ID = "categoryId";
    private int categoryId;
    private ListView listDrinks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink_category, container, false);

        listDrinks = root.findViewById(R.id.list_drinks);

        mData = getData();
        MyAdapter listAdapter = new MyAdapter(getContext());
        listDrinks.setAdapter(listAdapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cursor.close();
        Log.d("debug", "DrinkCategoryFragment销毁");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }


    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // map数据准备
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            //获取Bundle
            Bundle bundle = getArguments();
            categoryId = bundle.getInt(EXTRA_CATEGORY_ID);
            cursor = db.query(STUFF_TABLE, new String[]{"_id", "NAME"}, "CATEGORY=?",
                    new String[]{String.valueOf(categoryId)}, null, null, null, null);

            // 添加
            while (cursor.moveToNext()) {
                // 移动光标到下一行
                Map<String, Object> map = new HashMap<String, Object>();

                int stuffId = cursor.getInt(cursor.getColumnIndex("_id"));
                String stuffName = cursor.getString(cursor.getColumnIndex("NAME"));

                map.put("stuffId", stuffId);
                map.put("stuffName", stuffName);
                list.add(map);
            }
        } catch (SQLiteException e) {
            Log.e("sqlite", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }

        return list;
    }


    public static final class ViewHolder {
        TextView tv_stuffId;
        TextView tv_stuffName;
        TextView tv_add_to_cart;
    }

    public class MyAdapter extends BaseAdapter {


        private LayoutInflater mInflater;

        MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.stuff_list_item_layout, null);

                holder.tv_stuffId = convertView.findViewById(R.id.stuff_id);
                holder.tv_stuffName = convertView.findViewById(R.id.stuff_name);
                holder.tv_add_to_cart = convertView.findViewById(R.id.tv_add_to_cart);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }


            holder.tv_stuffName.setText((String) mData.get(position).get("stuffName"));
            holder.tv_stuffId.setText(String.valueOf(mData.get(position).get("stuffId")));

            holder.tv_stuffName.setTag(position);
            holder.tv_add_to_cart.setTag(position);


            holder.tv_add_to_cart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 加入购物车
                    Log.d("debug", "加入购物车" + v.getTag());
                    int stuffId = (int) mData.get((Integer) v.getTag()).get("stuffId");
                    addToCart(stuffId, getActivity());
                }
            });

            holder.tv_stuffName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入详情页面
                    Log.d("debug", "进入详情页面" + v.getTag());
                    //开启事务跳转
                    assert getFragmentManager() != null;
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    StuffFragment stuffFragment = new StuffFragment();
                    Bundle bundle = new Bundle();

                    int stuffId = (int) mData.get((Integer) v.getTag()).get("stuffId");
                    bundle.putInt(StuffFragment.EXTRA_STUFFID, stuffId);
                    stuffFragment.setArguments(bundle);

                    transaction
                            .addToBackStack(null)  //将当前fragment加入到返回栈中
                            .replace(R.id.nav_host_fragment, stuffFragment)
                            .show(stuffFragment)
                            .commit();
                }

            });
            return convertView;
        }
    }

    // 购物车
    public static void refreshCartNum(TextView tv_num, Context context){
//        TextView tv_num = Objects.requireNonNull(getActivity().findViewById(R.id.tv_num);
        int num = getCartItemNum(context);
        tv_num.setText(String.valueOf(num));
        tv_num.setVisibility(num ==0 ? View.INVISIBLE: View.VISIBLE);
    }

    private static List<Map<String, Object>> getCartNData(Context context) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Cursor cursor1;
        // map数据准备
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(context);

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            // 全部取出来
            cursor1 = db.query(CART_TABLE, new String[]{"ID", "STUFF_ID", "NUMBER"}, null,
                    null, null, null, null, null);

            while (cursor1.moveToNext()) {
                // 添加
                // 需要的是：cartId, stuffId, stuffName(display), stuffPrice, stuffImgId, stuffNumber
                // 移动光标到下一行
                Map<String, Object> map = new HashMap<String, Object>();

                int cartId = cursor1.getInt(cursor1.getColumnIndex("ID"));
                int stuffId = cursor1.getInt(cursor1.getColumnIndex("STUFF_ID"));
                int stuffNumber = cursor1.getInt(cursor1.getColumnIndex("NUMBER"));
                map.put("cartId", cartId);
                map.put("stuffId", stuffId);
                map.put("stuffNumber", stuffNumber);
                list.add(map);
            }

        } catch (
                SQLiteException e) {
            Log.e("debug", Objects.requireNonNull(e.getMessage()));
        }
        return list;
    }

    private static int getCartItemNum(Context context){
        int numTol = 0;
        List<Map<String, Object>> list = getCartNData(context);
        for (Map<String, Object> map : list) {
            numTol += (int)map.get("stuffNumber");
        }
        return numTol;
    }


    // 添加一件商品到购物车，不含refresh
    public static void addToCart(int stuffId, Context context){

        Cursor cursor;
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(context);

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            int oldNumber = 0;
            int cartId = 0;

            // 1. 查现在的number
            cursor = db.query(CART_TABLE, new String[]{"ID", "NUMBER"}, "STUFF_ID=?",
                    new String[]{String.valueOf(stuffId)},
                    null, null, null, null);

            if (cursor.moveToFirst()) { // 如果已经存在记录
                oldNumber = cursor.getInt(cursor.getColumnIndex("NUMBER"));
                cartId = cursor.getInt(cursor.getColumnIndex("ID"));
            }

            // 2. 准备数据
            ContentValues contentValues = new ContentValues();
            contentValues.put("STUFF_ID", stuffId);
            contentValues.put("NUMBER", oldNumber + 1);

            // 3.更新CART表
            if (oldNumber == 0) {
                // insert
                long result = db.insert(CART_TABLE, null, contentValues);
                Log.d("debug", "insert " + CART_TABLE + " " + result);
            } else {
                // update
                long result = db.update(CART_TABLE, contentValues, "ID=?",
                        new String[]{String.valueOf(cartId)});
                Log.d("debug", "update " + CART_TABLE + " " + result);
            }
            refreshCartNum((TextView) ((FragmentActivity)context).findViewById(R.id.tv_num), context);
        } catch (SQLiteException e) {
            Log.e("sqlite", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(context,"database unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}