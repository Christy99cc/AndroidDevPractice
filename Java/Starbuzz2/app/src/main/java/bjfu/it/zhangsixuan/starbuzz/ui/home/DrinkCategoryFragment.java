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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;

public class DrinkCategoryFragment extends Fragment {

    private Cursor cursor;

    static final String EXTRA_CATEGORY_NAME = "categoryName";
    private String categoryName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink_category, container, false);

        ListView listDrinks = root.findViewById(R.id.list_drinks);
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(getActivity());

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {
            /*
             * selection 的位置是null
             */

            //获取Bundle
            Bundle bundle = getArguments();
            categoryName = bundle.getString(EXTRA_CATEGORY_NAME);

            cursor = db.query(categoryName, new String[]{"_id", "NAME"}, null,
                    null, null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                    getActivity(),
                    // 内置布局，每行显示一个文本框
                    android.R.layout.simple_list_item_1,
                    // 数据源
                    cursor,
                    // 显示name
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0
            );

            // 指定适配器
            listDrinks.setAdapter(listAdapter);

        } catch (SQLiteException e) {
            Log.e("sqlite", e.getMessage());
            Toast.makeText(getActivity(), "database unavailable", Toast.LENGTH_SHORT).show();
        }

        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listDrinks,
                                    View view,
                                    int position,
                                    long id) {
                //开启事务跳转
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                DrinkFragment drinkFragment = new DrinkFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(DrinkFragment.EXTRA_DRINKID, (int) id);
                bundle.putString(EXTRA_CATEGORY_NAME, categoryName);
                drinkFragment.setArguments(bundle);

                transaction
                        .addToBackStack(null)  //将当前fragment加入到返回栈中
                        .replace(R.id.nav_host_fragment, drinkFragment)
                        .show(drinkFragment)
                        .commit();

                Toast.makeText(getActivity(), "click position:" + position, Toast.LENGTH_SHORT).show();
            }
        };

        // 设置监听器
        listDrinks.setOnItemClickListener(itemClickListener);

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
}
