package bjfu.it.zhangsixuan.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private int seconds = 0;

    private boolean running = false;

    private boolean wasRunning = false;

    /**
     * 调用onCreate()方法，将保存的Bundle作为参数传入
     * 从Bundle存储的值恢复销毁前的状态
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("life cycle:", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");

        }
        runTimer();
    }

    /**
     * 可见前调用onStart()，onStart()返回后Activity可见
     * 覆盖onStart()，在可见前继续计时
     */
    @Override
    protected void onStart() {
        Log.d("life cycle:", "onStart");
        super.onStart();
//        if (wasRunning) {
//            running = true;
//        }
    }

    /**
     * 获得焦点前调用onResume()，返回后Activity与用户交互
     */
    @Override
    protected void onResume() {
        Log.d("life cycle:", "onResume");
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }


    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.time_view);

        //创建UI线程的handler，用于消息处理
        final Handler handler = new Handler();
        handler.post(new Runnable() { //立即提交一个Runnable
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format("%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);

                if (running) {
                    seconds++;
                }

                //每隔1000ms，重复提交该任务
                handler.postDelayed(this, 1000);
            }
        });
    }

    /**
     * 失去焦点前调用onPause()，返回后Activity停止与用户交互
     */
    @Override
    protected void onPause() {
        Log.d("life cycle:", "onPause");
        super.onPause();
        wasRunning = running;
        running = false;
    }


    /**
     * 在消失前调用onStop()，onStop()返回后Activity消失
     * 切换到后台，停止计时
     */
    @Override
    protected void onStop() {
        Log.d("life cycle:", "onStop");
        /*
         * 覆盖生命周期方法前，必须先调用父类的生命周期
         */
        super.onStop();
//        wasRunning = running;  //记录可见时候的计时状态
//        running = false;  // 停止计时
    }

    @Override
    protected void onDestroy() {
        Log.d("life cycle:", "onDestroy");
        super.onDestroy();
    }


    /**
     * 销毁原Activity前，调用 onSaveInstanceState()保存实例变量
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }


    //点击开始计时
    public void onClickStart(View view) {
        running = true;
    }

    //点击停止计时
    public void onClickStop(View view) {
        running = false;
    }

    //重置计时器
    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }


}
