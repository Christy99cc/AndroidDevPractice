package bjfu.it.zhangsixuan.programadviserk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val programExpert = ProgramExpert()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // click
    fun onClickButton(button: View){

        var spinner = findViewById<Spinner>(R.id.feature)

        var textView = findViewById<TextView>(R.id.language)

        var feature = spinner.selectedItem.toString()

        var language:String = programExpert.getLanguage(feature)

        textView.setText(language)

    }
}
