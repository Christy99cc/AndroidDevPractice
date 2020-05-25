package bjfu.it.zhangsixuan.starbuzz.ui.home;

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
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

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
            Log.e("sqlite", e.getMessage());
            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }

        return list;
    }


    public final class ViewHolder {
        public TextView tv_stuffId;
        public TextView tv_stuffName;
        public TextView tv_add_to_cart;
    }

    public class MyAdapter extends BaseAdapter {


        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
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

                // 这句修改成parent了
                convertView = mInflater.inflate(R.layout.stuff_list_item_layout, null);

                holder.tv_stuffId = (TextView) convertView.findViewById(R.id.stuff_id);
                holder.tv_stuffName = (TextView) convertView.findViewById(R.id.stuff_name);
                holder.tv_add_to_cart = (TextView) convertView.findViewById(R.id.tv_add_to_cart);
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
                    Toast.makeText(getActivity(), "加入购物车"+v.getTag(), Toast.LENGTH_SHORT).show();
                }
            });

            holder.tv_stuffName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入详情页面
                    Toast.makeText(getActivity(), "进入详情页面"+v.getTag(), Toast.LENGTH_SHORT).show();
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
}
