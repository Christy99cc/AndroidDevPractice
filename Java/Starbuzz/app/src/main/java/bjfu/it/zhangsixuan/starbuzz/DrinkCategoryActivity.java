package bjfu.it.zhangsixuan.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DrinkCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_category);

        ArrayAdapter<Drink> listAdapter = new ArrayAdapter<>(
                this, //当前作用的对象
                android.R.layout.simple_list_item_1,  //内建布局资源
                Drink.drinks); //数据源

        ListView listDirnks = findViewById(R.id.list_drinks);
        listDirnks.setAdapter(listAdapter);

        // 监听器
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DrinkCategoryActivity.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int) id);
                startActivity(intent);
            }
        };

        // 设置监听器
        ListView listView = findViewById(R.id.list_drinks);
        listView.setOnItemClickListener(itemClickListener);

    }
}
