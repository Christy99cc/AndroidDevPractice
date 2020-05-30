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
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.adapter.Item2Adapter;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;

public class StuffCategoryFragment extends Fragment {
    private Cursor cursor;
    // 用于Bundle传递数据而设置
    public static final String EXTRA_CATEGORY_ID = "categoryId";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 加载布局fragment_stuff_category
        View root = inflater.inflate(R.layout.fragment_stuff_category, container, false);
        // 获取list_stuffs的引用
        ListView listStuffs = root.findViewById(R.id.list_stuffs);
        // 获取当前categoryId的商品数据
        List<Map<String, Object>> mData =  getData();
        // 保证不为空
        assert getFragmentManager() != null;
        // 开启事务跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 实例化Item2Adapter
        Item2Adapter listAdapter = new Item2Adapter(getContext(), mData, transaction);
        // 设置适配器
        listStuffs.setAdapter(listAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cursor.close();
        Log.d("debug", "DrinkCategoryFragment销毁");
    }

    /**
     * 获取当前categoryId的商品数据
     * @return
     */
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // map数据准备
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {
            //获取Bundle
            Bundle bundle = getArguments();
            assert bundle != null;
            int categoryId = bundle.getInt(EXTRA_CATEGORY_ID);
            // 在STUFF表中查询categoryId对应的商品id和name
            cursor = db.query(STUFF_TABLE, new String[]{"_id", "NAME"}, "CATEGORY=?",
                    new String[]{String.valueOf(categoryId)}, null, null, null, null);
            while (cursor.moveToNext()) {
                // 移动光标到下一行
                Map<String, Object> map = new HashMap<String, Object>();
                // 从cursor中获取stuffId和stuffName
                int stuffId = cursor.getInt(cursor.getColumnIndex("_id"));
                String stuffName = cursor.getString(cursor.getColumnIndex("NAME"));
                // put to map
                map.put("stuffId", stuffId);
                map.put("stuffName", stuffName);
                // add to list
                list.add(map);
            }
        } catch (SQLiteException e) {
            Log.e("sqlite", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }
        return list;
    }


}