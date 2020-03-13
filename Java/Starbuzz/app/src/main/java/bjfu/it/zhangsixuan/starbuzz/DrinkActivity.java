package bjfu.it.zhangsixuan.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKID = "drinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkId = getIntent().getIntExtra(EXTRA_DRINKID, 0);
        Drink drink = Drink.drinks[drinkId];

        // coffee image
        ImageView photo = findViewById(R.id.photo);
        photo.setImageResource(drink.getImageResource());
        photo.setContentDescription(drink.getName());

        // name
        TextView name = findViewById(R.id.name);
        name.setText(drink.getName());

        // description
        TextView description = findViewById(R.id.description);
        description.setText(drink.getDescription());
    }
}
