package bjfu.it.zhangsixuan.messager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReceiveMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_message);

        //get message
        Intent intent = getIntent();
        String message = intent.getStringExtra(CreateMessageActivty.MESSAGE_KEY);

        //set message
        TextView textView = findViewById(R.id.output);
        textView.setText(message);
    }
}
