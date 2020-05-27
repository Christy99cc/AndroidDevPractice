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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;


public class HomeFragment extends Fragment {

    private List<Map<String, Object>> mData;

    private int categoryId;
    private Cursor favoritesCursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final ListView listView = root.findViewById(R.id.list_options);

        // favorite
        setupFavoritesListView(root);

        // 为listView注册单击监听器
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "click position:" + position, Toast.LENGTH_SHORT)
                        .show();

                categoryId = position;

                //开启事务跳转
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                StuffCategoryFragment stuffCategoryFragment = new StuffCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(StuffCategoryFragment.EXTRA_CATEGORY_ID, categoryId);
                stuffCategoryFragment.setArguments(bundle);

                transaction
                        .addToBackStack(null)  //将当前fragment加入到返回栈中
                        .replace(R.id.nav_host_fragment, stuffCategoryFragment)
                        .show(stuffCategoryFragment)
                        .commit();
            }
        };
        listView.setOnItemClickListener(itemClickListener);
        return root;
    }

    private void setupFavoritesListView(View view) {
//        ListView listFavorites = view.findViewById(R.id.list_favorites);
        GridView gv_fav = view.findViewById(R.id.gv_fav);

        List<Map<String, Object>> mapList = getData();
        TextView textView = view.findViewById(R.id.favorite_label);
        if (mapList.size() == 0) {// 不显示favorite字样
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
//        SimpleAdapter favoriteAdapter = new SimpleAdapter(getContext(), getData(),
//                R.layout.fav_item_layout,
//                new String[]{"favorite_image", "favorite_name"},
//                new int[]{R.id.favorite_image, R.id.favorite_name});
//        listFavorites.setAdapter(favoriteAdapter);
        mData = getData();
        FavAdapter favAdapter = new FavAdapter(getContext());
        gv_fav.setAdapter(favAdapter);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();

            favoritesCursor = db.query(STUFF_TABLE,
                    new String[]{"_id", "NAME", "IMAGE_SOURCE_ID"}, "FAVORITE=1",
                    null, null, null, null, null);

            // 添加
            while (favoritesCursor.moveToNext()) {
                // 移动光标到下一行
                Map<String, Object> hashMap = new HashMap<String, Object>();

                int favId = favoritesCursor.getInt(favoritesCursor.getColumnIndex("_id"));
                String name = favoritesCursor.getString(favoritesCursor.getColumnIndex("NAME"));
                int imageId = favoritesCursor.getInt(favoritesCursor.getColumnIndex("IMAGE_SOURCE_ID"));

                hashMap.put("favorite_id", favId);
                hashMap.put("favorite_name", name);
                hashMap.put("favorite_image", imageId);
                list.add(hashMap);
            }

        } catch (SQLiteException e) {
            Log.d("debug", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }

        return list;
    }

    public static final class FavViewHolder {
        ImageView iv_fav_item;
        TextView tv_fav_item;
        TextView tv_fav_item_id;
    }

    public class FavAdapter extends BaseAdapter {


        private LayoutInflater mInflater;

        FavAdapter(Context context) {
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

            HomeFragment.FavViewHolder holder = null;
            if (convertView == null) {

                holder = new HomeFragment.FavViewHolder();

                convertView = mInflater.inflate(R.layout.fav_item_layout, null);

                holder.iv_fav_item = convertView.findViewById(R.id.iv_fav_item);
                holder.tv_fav_item = convertView.findViewById(R.id.tv_fav_item);
                holder.tv_fav_item_id = convertView.findViewById(R.id.tv_fav_item_id);
                convertView.setTag(holder);

            } else {
                holder = (HomeFragment.FavViewHolder) convertView.getTag();
            }


            holder.iv_fav_item.setImageResource((int) mData.get(position).get("favorite_image"));
            holder.tv_fav_item.setText((String)mData.get(position).get("favorite_name"));

            holder.iv_fav_item.setTag(position);
            holder.tv_fav_item.setTag(position);


            holder.tv_fav_item.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Log.d("debug", "进入详情页面" + v.getTag());
                    int stuffId = (int) mData.get((Integer) v.getTag()).get("favorite_id");
                    //TODO 进入详情
                    toDetailFragment(stuffId);

                }
            });

            holder.iv_fav_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入详情页面
                    Log.d("debug", "进入详情页面" + v.getTag());
                    int stuffId = (int) mData.get((Integer) v.getTag()).get("favorite_id");

                    //开启事务跳转
                    toDetailFragment(stuffId);
                }

            });
            return convertView;
        }
    }

    private void toDetailFragment(int stuffId){
        //开启事务跳转
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        StuffFragment stuffFragment = new StuffFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(StuffFragment.EXTRA_STUFFID, stuffId);
        stuffFragment.setArguments(bundle);

        transaction
                .addToBackStack(null)  //将当前fragment加入到返回栈中
                .replace(R.id.nav_host_fragment, stuffFragment)
                .show(stuffFragment)
                .commit();
    }

}
