package bjfu.it.zhangsixuan.starbuzz.ui.home;

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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.util.BannerUtils;
import com.youth.banner.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.adapter.ImageAdapter;
import bjfu.it.zhangsixuan.starbuzz.adapter.ItemAdapter;
import bjfu.it.zhangsixuan.starbuzz.bean.BannerBean;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;


public class HomeFragment extends Fragment {

    private int categoryId;
    Cursor cursor;

    private static List<BannerBean> BANNER_ITEMS = new ArrayList<BannerBean>() {
        {
            add(new BannerBean(1, "Latte", R.drawable.dk_latte_1));
            add(new BannerBean(2, "Cappuccino", R.drawable.dk_cappuccino_1));
            add(new BannerBean(3, "Macchiato", R.drawable.dk_macchiato_1));
            add(new BannerBean(4, "Sandwich", R.drawable.fd_sandwich_1));
        }
    };

    public static List<String> titles = new ArrayList<String>(){{
        add("Latte");
        add("Cappuccino");
        add("Macchiato");
        add("Sandwich");
    }};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initView(root);

        final ListView listView = root.findViewById(R.id.list_options);

        // favorite
        setupFavoritesGridView(root);

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

    private void setupFavoritesGridView(View view) {
        GridView gv_fav = view.findViewById(R.id.gv_fav);

        List<Map<String, Object>> mapList = getFavData();
        TextView textView = view.findViewById(R.id.favorite_label);
        if (mapList.size() == 0) {// 不显示favorite字样
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        ItemAdapter favAdapter = new ItemAdapter(getContext(), mapList, transaction);
        gv_fav.setAdapter(favAdapter);
    }

    private List<Map<String, Object>> getFavData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();

            cursor = db.query(STUFF_TABLE,
                    new String[]{"_id", "NAME", "IMAGE_SOURCE_ID"}, "FAVORITE=1",
                    null, null, null, null, null);

            // 添加
            while (cursor.moveToNext()) {
                // 移动光标到下一行
                Map<String, Object> hashMap = new HashMap<String, Object>();

                int favId = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("NAME"));
                int imageId = cursor.getInt(cursor.getColumnIndex("IMAGE_SOURCE_ID"));

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


    private void initView(View view) {

        final Banner banner = view.findViewById(R.id.banner);
        //--------------------------简单使用-------------------------------
        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(new ImageAdapter(BANNER_ITEMS))
                .setIndicator(new CircleIndicator(getActivity()))
                .setUserInputEnabled(true);

        banner.setOnBannerListener((data, position) -> {
            int stuffId = ((BannerBean) data).getId();
            //开启事务跳转
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Utils.toDetailFragment(stuffId, transaction);
        });

        banner.start();

    }

//    private List<BannerBean> getBannerData() {
//        List<BannerBean>list = new ArrayList<>();
//
//        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
//        try {
//            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
//
//            cursor = db.query(STUFF_TABLE,
//                    new String[]{"_id", "NAME", "IMAGE_SOURCE_ID"}, "CATEGORY=0",
//                    null, null, null, null, null);
//
//            // 添加
//            while (cursor.moveToNext()) {
//                // 移动光标到下一行
//                int id = cursor.getInt(cursor.getColumnIndex("_id"));
//                String name = cursor.getString(cursor.getColumnIndex("NAME"));
//                int imageId = cursor.getInt(cursor.getColumnIndex("IMAGE_SOURCE_ID"));
//                BannerBean bannerBean = new BannerBean(id, name, imageId);
//                list.add(bannerBean);
//            }
//
//        } catch (SQLiteException e) {
//            Log.d("debug", Objects.requireNonNull(e.getMessage()));
//        }
//        return list;
//    }

}
