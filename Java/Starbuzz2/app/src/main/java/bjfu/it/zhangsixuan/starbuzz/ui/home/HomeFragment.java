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
        ListView listFavorites = view.findViewById(R.id.list_favorites);

        List<Map<String, String>> mapList = getData();
        TextView textView = view.findViewById(R.id.favorite_label);
        if (mapList.size() == 0){// 不显示favorite字样
            textView.setVisibility(View.INVISIBLE);
        }else {
            textView.setVisibility(View.VISIBLE);
        }
        SimpleAdapter favoriteAdapter = new SimpleAdapter(getContext(), getData(),
                R.layout.favorite_list_item_layout,
                new String[]{"favorite_image", "favorite_name"},
                new int[]{R.id.favorite_image, R.id.favorite_name});
        listFavorites.setAdapter(favoriteAdapter);

    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();

            favoritesCursor = db.query(STUFF_TABLE,
                    new String[]{"_id", "NAME", "IMAGE_SOURCE_ID"}, "FAVORITE=1",
                    null, null, null, null, null);

            // 添加
            while (favoritesCursor.moveToNext()) {
                // 移动光标到下一行
                Map<String, String> hashMap = new HashMap<String, String>();

                String name = favoritesCursor.getString(favoritesCursor.getColumnIndex("NAME"));
                int imageId = favoritesCursor.getInt(favoritesCursor.getColumnIndex("IMAGE_SOURCE_ID"));

                hashMap.put("favorite_name", name);
                hashMap.put("favorite_image", String.valueOf(imageId));
                list.add(hashMap);
            }

        } catch (SQLiteException e) {
            Log.d("sqlite", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }

        return list;
    }

}
