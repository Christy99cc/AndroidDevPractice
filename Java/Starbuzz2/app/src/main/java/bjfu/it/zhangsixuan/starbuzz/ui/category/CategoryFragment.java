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
import bjfu.it.zhangsixuan.starbuzz.ui.home.HomeFragment;

import static bjfu.it.zhangsixuan.starbuzz.MainActivity.STUFF_TABLE;

public class CategoryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        setupCategoryView(root);

        return root;
    }

    private void setupCategoryView(View view) {

        List<Map<String, Object>> mDkData = getData(0);
        List<Map<String, Object>> mFdData = getData(1);
        List<Map<String, Object>> mStData = getData(2);

        GridView gv_dk = view.findViewById(R.id.gv_category_drink);
        GridView gv_fd = view.findViewById(R.id.gv_category_food);
        GridView gv_st = view.findViewById(R.id.gv_category_store);

        TextView tv_dk = view.findViewById(R.id.category_drink_label);
        TextView tv_fd = view.findViewById(R.id.category_food_label);
        TextView tv_st = view.findViewById(R.id.category_store_label);

        // 显示label字样
        tv_dk.setVisibility(mDkData.size() == 0?View.INVISIBLE:View.VISIBLE);
        tv_fd.setVisibility(mFdData.size() == 0?View.INVISIBLE:View.VISIBLE);
        tv_st.setVisibility(mStData.size() == 0?View.INVISIBLE:View.VISIBLE);


        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        HomeFragment.FavAdapter dkAdapter = new HomeFragment.FavAdapter(getContext(), mDkData, transaction);
        HomeFragment.FavAdapter fdAdapter = new HomeFragment.FavAdapter(getContext(), mFdData, transaction);
        HomeFragment.FavAdapter stAdapter = new HomeFragment.FavAdapter(getContext(), mStData, transaction);

        gv_dk.setAdapter(dkAdapter);
        gv_fd.setAdapter(fdAdapter);
        gv_st.setAdapter(stAdapter);
    }

    private List<Map<String, Object>> getData(int categoryId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Cursor cursor;
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();

            cursor = db.query(STUFF_TABLE,
                    new String[]{"_id", "NAME", "IMAGE_SOURCE_ID"}, "CATEGORY=?",
                    new String[]{String.valueOf(categoryId)},
                    null, null, null, null);

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
//            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }

        return list;
    }
}
