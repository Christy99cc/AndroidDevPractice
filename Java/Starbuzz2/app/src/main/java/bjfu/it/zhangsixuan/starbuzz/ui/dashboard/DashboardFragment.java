package bjfu.it.zhangsixuan.starbuzz.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import bjfu.it.zhangsixuan.starbuzz.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final ListView listView = root.findViewById(R.id.list_options);

        // 为listView注册单击监听器
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "click position:" + position, Toast.LENGTH_SHORT)
                        .show();
                if (position == 0) { // Drinks
//                    Intent intent = new Intent(getActivity(),
//                            DrinkCategoryActivity.class);
//                    startActivity(intent);
                    //开启事务跳转
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    String textItem =  ((TextView) view).getText().toString();
                    DrinkCategoryFragment produceDetailFragment = new DrinkCategoryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("productTitle", textItem);
                    produceDetailFragment.setArguments(bundle);

                    transaction
                            .addToBackStack(null)  //将当前fragment加入到返回栈中
                            .replace(R.id.nav_host_fragment,produceDetailFragment)
                            .show(produceDetailFragment)
                            .commit();
                }
            }
        };
        listView.setOnItemClickListener(itemClickListener);
        return root;
    }
}
