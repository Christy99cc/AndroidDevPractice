package bjfu.it.zhangsixuan.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_0;   // 0数字按钮
    private Button btn_1;   // 1数字按钮
    private Button btn_2;   // 2数字按钮
    private Button btn_3;   // 3数字按钮
    private Button btn_4;   // 4数字按钮
    private Button btn_5;   // 5数字按钮
    private Button btn_6;   // 6数字按钮
    private Button btn_7;   // 7数字按钮
    private Button btn_8;   // 8数字按钮
    private Button btn_9;   // 9数字按钮
    private Button btn_dot; // 小数点按钮

    private Button btn_ce;          // 全部清空按钮
    private Button btn_c;           // 清空输入按钮

    private Button btn_plus;        // +按钮
    private Button btn_minus;       // -按钮
    private Button btn_mul;         // *按钮
    private Button btn_div;         // /按钮
    private Button btn_equal;       // =按钮
    private ImageButton btn_sqrt;        // √按钮

    private TextView showHistory;   // 显示历史
    private TextView showNow;       // 显示结果

    boolean ifClickOp;              // 是否点击了操作符

    String express; // 在history里展现的表达式


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // 相应的引用 19个
        btn_0 = findViewById(R.id.button_num_0);   // 0数字按钮
        btn_1 = findViewById(R.id.button_num_1);   // 1数字按钮
        btn_2 = findViewById(R.id.button_num_2);   // 2数字按钮
        btn_3 = findViewById(R.id.button_num_3);   // 3数字按钮
        btn_4 = findViewById(R.id.button_num_4);   // 4数字按钮
        btn_5 = findViewById(R.id.button_num_5);   // 5数字按钮
        btn_6 = findViewById(R.id.button_num_6);   // 6数字按钮
        btn_7 = findViewById(R.id.button_num_7);   // 7数字按钮
        btn_8 = findViewById(R.id.button_num_8);   // 8数字按钮
        btn_9 = findViewById(R.id.button_num_9);   // 9数字按钮

        btn_dot = findViewById(R.id.button_dot);         // 小数点按钮

        btn_ce = findViewById(R.id.button_ce);          // 全部清空按钮
        btn_c = findViewById(R.id.button_c);           // 清空输入按钮

        btn_plus = findViewById(R.id.button_plus);        // +按钮
        btn_minus = findViewById(R.id.button_minus);       // -按钮
        btn_mul = findViewById(R.id.button_mul);     // *按钮
        btn_div = findViewById(R.id.button_div);      // /按钮
        btn_equal = findViewById(R.id.button_equal);       // =按钮

        btn_sqrt = findViewById(R.id.button_sqrt);       // √按钮

        showHistory = findViewById(R.id.show_history);   // 显示历史
        showNow = findViewById(R.id.show_now);       // 显示正在输入的

        // 监听器
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_dot.setOnClickListener(this);
        btn_c.setOnClickListener(this);
        btn_ce.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_mul.setOnClickListener(this);
        btn_div.setOnClickListener(this);
        btn_equal.setOnClickListener(this);
        btn_sqrt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String input = showNow.getText().toString();

        // 点击的按钮
        switch (v.getId()) {

            case R.id.button_num_0:
            case R.id.button_num_1:
            case R.id.button_num_2:
            case R.id.button_num_3:
            case R.id.button_num_4:
            case R.id.button_num_5:
            case R.id.button_num_6:
            case R.id.button_num_7:
            case R.id.button_num_8:
            case R.id.button_num_9:
            case R.id.button_dot:
                // 数字部分
                input = input + ((Button) v).getText().toString();
                showNow.setText(input);
                break;

            case R.id.button_mul:
            case R.id.button_div:
            case R.id.button_plus:
            case R.id.button_minus:
                // 加减乘除
                express = showHistory.getText().toString() + input + ((Button) v).getText().toString();
                showHistory.setText(express);
                showNow.setText("");
                break;

            case R.id.button_sqrt:
                // 根号
                express = showHistory.getText().toString() + '√' + '(' + input + ')';
                showHistory.setText(express);
                showNow.setText("");
                break;

            case R.id.button_c:
                showHistory.setText("");
                showNow.setText("");
                break;

            case R.id.button_ce:
                showNow.setText("");
                break;

            case R.id.button_equal:
                // 加减乘除
                express = showHistory.getText().toString() + input + ((Button) v).getText().toString();
                showHistory.setText(express);
                calculate();
                break;
        }

    }

    private void calculate() {
        String expression = showHistory.getText().toString();
        String result = MyCalculator.getResult(expression);
        if (isNumber(result)) {
            // 展示结果
            showNow.setText(result);
        } else {
            // 显示提示信息
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, result, duration).show();
        }

    }

    // 判断String是否为数值类型，包括浮点数和负数
    private boolean isNumber(String regex) {
        return regex.matches("-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?");
    }
}
