package bjfu.it.zhangsixuan.starbuzz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bjfu.it.zhangsixuan.starbuzz.db.StarbuzzDatabaseHelper;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String STUFF_TABLE = "STUFF";
    public static final String CART_TABLE = "CART";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Sentry.init("https://e0eb3feae6b04481aa02a2a00e999431@sentry.sparking.app/4",
                new AndroidSentryClientFactory(this));
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        refreshCartNum();
    }

    // 购物车
    public void refreshCartNum(){
        TextView tv_num = findViewById(R.id.tv_num);
        int num = getCartItemNum();
        tv_num.setText(String.valueOf(num));
        tv_num.setVisibility(num ==0 ? View.INVISIBLE: View.VISIBLE);
    }

    private List<Map<String, Object>> getCartNData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Cursor cursor;
        // map数据准备
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);

        try (SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase()) {

            // 全部取出来
            cursor = db.query(CART_TABLE, new String[]{"ID", "STUFF_ID", "NUMBER"}, null,
                    null, null, null, null, null);

            while (cursor.moveToNext()) {
                // 添加
                // 需要的是：cartId, stuffId, stuffName(display), stuffPrice, stuffImgId, stuffNumber
                // 移动光标到下一行
                Map<String, Object> map = new HashMap<String, Object>();

                int cartId = cursor.getInt(cursor.getColumnIndex("ID"));
                int stuffId = cursor.getInt(cursor.getColumnIndex("STUFF_ID"));
                int stuffNumber = cursor.getInt(cursor.getColumnIndex("NUMBER"));
                map.put("cartId", cartId);
                map.put("stuffId", stuffId);
                map.put("stuffNumber", stuffNumber);
                list.add(map);
            }

        } catch (
                SQLiteException e) {
            Log.e("sqlite", Objects.requireNonNull(e.getMessage()));
            makeText(this, "database unavailable", LENGTH_SHORT).show();
        }
        return list;
    }

    private int getCartItemNum(){
        int numTol = 0;
        List<Map<String, Object>> list = getCartNData();
        for (Map<String, Object> map : list) {
            numTol += (int)map.get("stuffNumber");
        }
        return numTol;
    }

    @Override
    public void onClick(View v) {
        /* 只要点击就刷新购物车数量
         */
        refreshCartNum();
    }
}