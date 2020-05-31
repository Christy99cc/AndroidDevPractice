package bjfu.it.zhangsixuan.starbuzz.ui.category;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.adapter.ItemAdapter;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;

public class CategoryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 加载分类的布局fragment_category
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        // 设置CategoryView
        setupCategoryView(root);
        return root;
    }

    /**
     * 建立CategoryView
     * @param view
     */
    private void setupCategoryView(View view) {
        // 根据categoryId获取商品数据
        List<Map<String, Object>> mDkData = getData(0);
        List<Map<String, Object>> mFdData = getData(1);
        List<Map<String, Object>> mStData = getData(2);
        // 获取各自网格布局的引用
        GridView gv_dk = view.findViewById(R.id.gv_category_drink);
        GridView gv_fd = view.findViewById(R.id.gv_category_food);
        GridView gv_st = view.findViewById(R.id.gv_category_store);
        // 获取类别标签的显示
        TextView tv_dk = view.findViewById(R.id.category_drink_label);
        TextView tv_fd = view.findViewById(R.id.category_food_label);
        TextView tv_st = view.findViewById(R.id.category_store_label);
        // 根据数据有无设置类别标签是否显示
        tv_dk.setVisibility(mDkData.size() == 0?View.INVISIBLE:View.VISIBLE);
        tv_fd.setVisibility(mFdData.size() == 0?View.INVISIBLE:View.VISIBLE);
        tv_st.setVisibility(mStData.size() == 0?View.INVISIBLE:View.VISIBLE);
        // 在保证不空的情况下，开启事务跳转
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 根据各自数据的不同，实例化适配器
        ItemAdapter dkAdapter = new ItemAdapter(getContext(), mDkData, transaction);
        ItemAdapter fdAdapter = new ItemAdapter(getContext(), mFdData, transaction);
        ItemAdapter stAdapter = new ItemAdapter(getContext(), mStData, transaction);
        // 为各自网格布局设置适配器
        gv_dk.setAdapter(dkAdapter);
        gv_fd.setAdapter(fdAdapter);
        gv_st.setAdapter(stAdapter);
    }

    /**
     * 根据categoryId获取商品的数据
     * @param categoryId
     * @return
     */
    private List<Map<String, Object>> getData(int categoryId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Cursor cursor;
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
            // 根据categoryId在STUFF表中查出商品的id、name、image
            cursor = db.query(STUFF_TABLE,
                    new String[]{"_id", "NAME", "IMAGE_SOURCE_ID"}, "CATEGORY=?",
                    new String[]{String.valueOf(categoryId)},
                    null, null, null, null);
            while (cursor.moveToNext()) {
                // 移动光标到下一行            // map数据准备
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
//            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }
        return list;
    }
}
