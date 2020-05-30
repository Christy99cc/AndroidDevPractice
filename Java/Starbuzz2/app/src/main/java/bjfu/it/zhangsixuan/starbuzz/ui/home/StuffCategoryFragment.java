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

    public static final String EXTRA_CATEGORY_ID = "categoryId";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stuff_category, container, false);

        ListView listDrinks = root.findViewById(R.id.list_drinks);
        List<Map<String, Object>> mData =  getData();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Item2Adapter listAdapter = new Item2Adapter(getContext(), mData, transaction);
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
        super.onActivityCreated(savedInstanceState);
    }


    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        // map数据准备
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            //获取Bundle
            Bundle bundle = getArguments();
            assert bundle != null;
            int categoryId = bundle.getInt(EXTRA_CATEGORY_ID);
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


}