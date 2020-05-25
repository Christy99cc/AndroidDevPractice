package bjfu.it.zhangsixuan.starbuzz.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.adapter.FavoriteAdapter;
import bjfu.it.zhangsixuan.starbuzz.bean.FavoriteBean;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

import static bjfu.it.zhangsixuan.starbuzz.ui.home.DrinkCategoryFragment.EXTRA_CATEGORY_NAME;

public class HomeFragment extends Fragment {

    private String categoryName;
    private Cursor favoritesCursor;
    private SQLiteDatabase db;


    private static final ArrayList<String> tables = new ArrayList<String>();

    static {
        tables.add("DRINK");
        tables.add("FOOD");
        tables.add("STORE");
    }


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
                if (position == 0) { // Drinks
                    categoryName = "DRINK";
                } else if (position == 1) {
                    categoryName = "FOOD";
                } else if (position == 2) {
                    categoryName = "STORE";
                }

                //开启事务跳转
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                DrinkCategoryFragment drinkCategoryFragment = new DrinkCategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_CATEGORY_NAME, categoryName);
                drinkCategoryFragment.setArguments(bundle);

                transaction
                        .addToBackStack(null)  //将当前fragment加入到返回栈中
                        .replace(R.id.nav_host_fragment, drinkCategoryFragment)
                        .show(drinkCategoryFragment)
                        .commit();
            }
        };
        listView.setOnItemClickListener(itemClickListener);
        return root;
    }

    private void setupFavoritesListView(View view) {
        ListView listFavorites = view.findViewById(R.id.list_favorites);


//      FavoriteAdapter favoriteAdapter = new FavoriteAdapter(hashMap);
        SimpleAdapter favoriteAdapter = new SimpleAdapter(getContext(), getData(),
                R.layout.favorite_linear_layout,
                new String[]{"favorite_image", "favorite_name"},
                new int[]{R.id.favorite_image, R.id.favorite_name});
        listFavorites.setAdapter(favoriteAdapter);

    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        try {
            db = starbuzzDatabaseHelper.getReadableDatabase();

            // 遍历数据表
            for (String table : tables) {
                favoritesCursor = db.query(table,
                        new String[]{"_id", "NAME", "IMAGE_SOURCE_ID"}, "FAVORITE=1",
                        null, null, null, null, null);

                // 添加
                while (favoritesCursor.moveToNext()) {
                    // 移动光标到下一行
                    Map<String, String> hashMap = new HashMap<String, String>();

                    String id = String.valueOf(favoritesCursor.getInt(0));
                    String name = favoritesCursor.getString(favoritesCursor.getColumnIndex("NAME"));
                    int imageId = favoritesCursor.getInt(favoritesCursor.getColumnIndex("IMAGE_SOURCE_ID"));
//                    FavoriteBean bean = new FavoriteBean(Integer.parseInt(id), name, imageId);
//                    hashMap.put("id", id);
                    hashMap.put("favorite_name", name);
                    hashMap.put("favorite_image", String.valueOf(imageId));
                    list.add(hashMap);
                }
            }
        } catch (SQLiteException e) {
            Log.d("sqlite", e.getMessage());
            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }


        return list;
    }

}
