package bjfu.it.zhangsixuan.messager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateMessageActivty extends AppCompatActivity {

    public static final String MESSAGE_KEY = "bjfu.it.zsx.messager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
    }

    public void onSendMessage(View view) {
        Intent intent = new Intent(this, ReceiveMessageActivity.class);

        EditText editText = findViewById(R.id.input);

        String message = editText.getText().toString();

        intent.putExtra(MESSAGE_KEY, message);

        startActivity(intent);
    }

    public void onSendAction(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        EditText editText = findViewById(R.id.input);
        String message = editText.getText().toString();
        intent.putExtra(Intent.EXTRA_TEXT, message);
        String chooseTitle = getString(R.string.chooser);
        Intent chosenIntent = Intent.createChooser(intent, chooseTitle);
        startActivity(chosenIntent);
    }
}
