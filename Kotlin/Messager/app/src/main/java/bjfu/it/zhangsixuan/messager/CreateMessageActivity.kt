package bjfu.it.zhangsixuan.messager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_create_message.*

class CreateMessageActivity : AppCompatActivity() {
    companion object {
        const val MESSAGE_KEY:String = "bjfu.it.zsx.messager"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_message)
    }

    fun onSendMessage(view: View) {
        val intent = Intent(this, ReceiveMessageActivity::class.java)
        intent.putExtra(MESSAGE_KEY, input.text.toString())
        startActivity(intent)
    }
}
