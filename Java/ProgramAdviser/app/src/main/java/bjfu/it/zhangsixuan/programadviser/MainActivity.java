package bjfu.it.zhangsixuan.programadviser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final ProgramExpert programExpert = new ProgramExpert();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClickButton(View button) {

        // get spinner 引用
        Spinner spinner = findViewById(R.id.feature);

        //get spinner 选项
        String feature = spinner.getSelectedItem().toString();

        //get textView 引用
        TextView textView = findViewById(R.id.language);

        // 查询model
        String language = programExpert.getLanguage(feature);

        // set text
        textView.setText(language);
    }
}
