package bjfu.it.zhangsixuan.starbuzz.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import bjfu.it.zhangsixuan.starbuzz.R;

import static bjfu.it.zhangsixuan.starbuzz.ui.home.DrinkCategoryFragment.EXTRA_CATEGORY_NAME;

public class HomeFragment extends Fragment {
    private String categoryName;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final ListView listView = root.findViewById(R.id.list_options);

        // 为listView注册单击监听器
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "click position:" + position, Toast.LENGTH_SHORT)
                        .show();
                if (position == 0) { // Drinks
                    categoryName = "DRINK";
                }else if (position == 1){
                    categoryName = "FOOD";
                }else if(position == 2){
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
                        .replace(R.id.nav_host_fragment,drinkCategoryFragment)
                        .show(drinkCategoryFragment)
                        .commit();
            }
        };
        listView.setOnItemClickListener(itemClickListener);
        return root;
    }
}
