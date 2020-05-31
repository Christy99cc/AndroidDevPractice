package bjfu.it.zhangsixuan.starbuzz;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import bjfu.it.zhangsixuan.starbuzz.music.MyBroadcastReceiver;
import bjfu.it.zhangsixuan.starbuzz.music.PlayingMusicServices;
import bjfu.it.zhangsixuan.starbuzz.utils.Utils;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

public class MainActivity extends AppCompatActivity {
    public static final String STUFF_TABLE = "STUFF";
    public static final String CART_TABLE = "CART";

    public static final String DSN = "https://e0eb3feae6b04481aa02a2a00e999431@sentry.sparking.app/4";


    /**
     * 规定开始音乐、暂停音乐、结束音乐的标志
     */
    public static final int PLAY_MUSIC = 1;
    public static final int PAUSE_MUSIC = 2;
    public static final int STOP_MUSIC = 3;
    public static int musicState = STOP_MUSIC;

    // 定义广播接受器
    private MyBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Sentry.init(DSN, new AndroidSentryClientFactory(this));
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_category, R.id.navigation_cart)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // 购物车件数
        Utils.refreshCartNum(findViewById(R.id.tv_num), this);

        /**
         * 背景音乐
         */
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.complete");
        // 注册
        registerReceiver(receiver, filter);
        // 获取引用
        Button btn_music = findViewById(R.id.btn_music);
        // 点击
        btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "music");
                musicState = (musicState == STOP_MUSIC) ? PLAY_MUSIC : STOP_MUSIC;
                playingmusic(musicState);
            }
        });
    }


    private void playingmusic(int type) {
        //启动服务，播放音乐
        Intent intent = new Intent(this, PlayingMusicServices.class);
        intent.putExtra("type", type);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


}