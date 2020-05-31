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
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.adapter.ImageTitleAdapter;
import bjfu.it.zhangsixuan.starbuzz.adapter.Item3Adapter;
import bjfu.it.zhangsixuan.starbuzz.adapter.ItemAdapter;
import bjfu.it.zhangsixuan.starbuzz.bean.DataBean;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;


public class HomeFragment extends Fragment {

    private Cursor cursor;

    private static List<DataBean> BANNER_ITEMS = new ArrayList<DataBean>() {
        {
            add(new DataBean(1, "Latte", R.drawable.dk_latte_1));
            add(new DataBean(2, "Cappuccino", R.drawable.dk_cappuccino_1));
            add(new DataBean(3, "Macchiato", R.drawable.dk_macchiato_1));
            add(new DataBean(4, "Sandwich", R.drawable.fd_sandwich_1));
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 加载布局文件
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // banner 横幅轮播
        setupBanner(root);
        // list options 类别展示
        setupCategoryGridView(root);
        // favorite 收藏展示
        setupFavoritesGridView(root);
        return root;
    }

    /**
     * 为类别选项建立单栏网格布局
     *
     * @param view
     */
    private void setupCategoryGridView(View view) {
        // 获取类别选项的网格布局的引用
        GridView gv_cate = view.findViewById(R.id.list_options);
        // 获取类别选项的数据
        List<Map<String, Object>> mapList = getCateData();
        //保证不为空
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 设置自定义适配器，包含事务跳转
        Item3Adapter cateAdapter = new Item3Adapter(getContext(), mapList, transaction);
        gv_cate.setAdapter(cateAdapter);
        // 获取网格布局的高度，并根据实际内容来进行调整（为了解决与ScrollView的冲突问题）
        setGridViewHeightBasedOnChildren(gv_cate, 1);
    }

    /**
     * 获取类别选项的数据
     *
     * @return list
     */
    private List<Map<String, Object>> getCateData() {
        List<Map<String, Object>> list = new ArrayList<>();
        // drink
        Map<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("image", R.drawable.drink);
        hashMap1.put("name", "Drink");
        list.add(hashMap1);
        // food
        Map<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("image", R.drawable.food);
        hashMap2.put("name", "Food");
        list.add(hashMap2);
        // store
        Map<String, Object> hashMap3 = new HashMap<>();
        hashMap3.put("image", R.drawable.store);
        hashMap3.put("name", "Store");
        list.add(hashMap3);
        return list;
    }

    /**
     * 为收藏部分建立网格布局，展示图片和商品名
     * @param view
     */
    private void setupFavoritesGridView(View view) {
        // 获取gv_fav的引用
        GridView gv_fav = view.findViewById(R.id.gv_fav);
        // 获取收藏的相关数据，返回List
        List<Map<String, Object>> mapList = getFavData();
        // 获取favorite的标签的引用
        TextView textView = view.findViewById(R.id.favorite_label);
        // 根据mapList中有无数据来设置是否显示favorite的标签
        textView.setVisibility(mapList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        // 保证不为空，开启事务
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 实例化自定义适配器
        ItemAdapter favAdapter = new ItemAdapter(getContext(), mapList, transaction);
        // 设置适配器
        gv_fav.setAdapter(favAdapter);
        // 获取网格布局的高度，并根据实际内容来进行调整（为了解决与ScrollView的冲突问题）
        setGridViewHeightBasedOnChildren(gv_fav, 3);
    }

    /**
     * 从数据库中获取收藏数据
     * @return
     */
    private List<Map<String, Object>> getFavData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
            // 查询STUFF表中FAVORITE=1的记录
            cursor = db.query(STUFF_TABLE,
                    new String[]{"_id", "NAME", "IMAGE_SOURCE_ID"}, "FAVORITE=1",
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                // 移动光标到下一行
                Map<String, Object> hashMap = new HashMap<String, Object>();
                // 读取cursor中的数据
                int favId = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("NAME"));
                int imageId = cursor.getInt(cursor.getColumnIndex("IMAGE_SOURCE_ID"));
                // put to hashmap
                hashMap.put("favorite_id", favId);
                hashMap.put("favorite_name", name);
                hashMap.put("favorite_image", imageId);
                // add to list
                list.add(hashMap);
            }
        } catch (SQLiteException e) {
            Log.d("debug", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }
        return list;
    }


    // 设置轮播的横幅
    private void setupBanner(View view) {
        // 获取banner的引用
        Banner<Object, BannerAdapter> banner = view.findViewById(R.id.banner);
        // 实例化适配器，包含图片和文字的
        ImageTitleAdapter imageTitleAdapter = new ImageTitleAdapter(BANNER_ITEMS);
        banner.addBannerLifecycleObserver(this); //添加生命周期观察者
        banner.setAdapter(imageTitleAdapter);  // 设置适配器
        banner.setIndicator(new CircleIndicator(getActivity()))
                .setUserInputEnabled(true); // 允许手动滑动
        // 点击事件
        banner.setOnBannerListener((data, position) -> {
            int stuffId = ((DataBean) data).getId();
            // 开启事务跳转
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // 根据stuffId进入详情页面
            Utils.toDetailFragment(stuffId, transaction);
        });
        banner.start();
    }

    /*
     * 动态修改GridView的高度
     */
    private void setGridViewHeightBasedOnChildren(GridView gridView, int col) {
        // 获取Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 设置高度
        int tolHeight = 0;
        // 行数
        int num = (int) (Math.ceil(listAdapter.getCount() / (1.0 * col)));
        for (int i = 0; i < num; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            tolHeight += listItem.getMeasuredHeight() + 15;
        }
        // 获取参数
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // 设置高度
        params.height = tolHeight;
        // 设置参数
        gridView.setLayoutParams(params);
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
