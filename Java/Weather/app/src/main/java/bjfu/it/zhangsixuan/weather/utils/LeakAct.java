package bjfu.it.zhangsixuan.weather.utils;

import android.widget.TextView;

import java.lang.ref.WeakReference;

public class LeakAct {
    private static LeakAct singleton;

    private LeakAct(){}

    public static LeakAct getInstance(){
        if (singleton == null)
            singleton = new LeakAct();
        return singleton;
    }

    /**
     * 单例引起的内存泄露
     */
    private TextView mTextViewLeak = null;
    public void setmTextViewLeak(TextView textView){
        mTextViewLeak = textView;
    }

    /**
     * 修复--单例引起的内存泄露
     */

    private WeakReference<TextView> mTextView = null;
    public void setmTextView(TextView textView){
        mTextView = new WeakReference<TextView>(textView);
    }
}
