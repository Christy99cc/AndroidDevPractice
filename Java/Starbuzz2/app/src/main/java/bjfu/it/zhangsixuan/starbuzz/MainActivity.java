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
import static bjfu.it.zhangsixuan.starbuzz.ui.home.StuffCategoryFragment.refreshCartNum;

public class MainActivity extends AppCompatActivity {
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

        // 购物车件数
        refreshCartNum((TextView) findViewById(R.id.tv_num), this);
    }


}