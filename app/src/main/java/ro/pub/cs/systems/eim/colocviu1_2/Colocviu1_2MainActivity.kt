package ro.pub.cs.systems.eim.colocviu1_2

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ro.pub.cs.systems.eim.colocviu1_2.Constants.All_TERMS_STATE
import ro.pub.cs.systems.eim.colocviu1_2.Constants.BROADCAST_RECEIVER_EXTRA
import ro.pub.cs.systems.eim.colocviu1_2.Constants.BROADCAST_RECEIVER_TAG
import ro.pub.cs.systems.eim.colocviu1_2.Constants.SERVICE_STARTED
import ro.pub.cs.systems.eim.colocviu1_2.Constants.SERVICE_STOPPED
import ro.pub.cs.systems.eim.colocviu1_2.Constants.SUM_RESULT_STATE
import java.util.Objects

class Colocviu1_2MainActivity : AppCompatActivity() {
    lateinit var nextTermEditText: EditText
    lateinit var allTermViewText: TextView
    lateinit var addButton: Button
    lateinit var computeButton: Button
    private val intentFilter = IntentFilter()
    private var serviceStatus: Int = SERVICE_STOPPED
    var sumState: Int = 0
    var oldAllTerms:String = ""

    private val buttonClickListener = ButtonClickListener()

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val nextTermEdit = nextTermEditText.text.toString()
            var allTermView = allTermViewText.text.toString()
            when (view.id) {
                R.id.AddButton -> {
                    if(nextTermEdit != ""){
                        oldAllTerms = allTermView
                        allTermView += " + $nextTermEdit"
                        allTermViewText.setText(allTermView)
                    }
                }
            }
        }
    }
    private val messageBroadcastReceiver = MessageBroadcastReceiver()
    private class MessageBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Toast.makeText(null, "Received broad", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test01_2_main)

        nextTermEditText = findViewById<EditText>(R.id.nextTermText)
        allTermViewText = findViewById<TextView>(R.id.allTermsText)

        addButton = findViewById<Button>(R.id.AddButton)
        computeButton = findViewById<Button>(R.id.ComputeButton)
        addButton.setOnClickListener(buttonClickListener)

        val activityResultsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            val sum = data?.getIntExtra("SUM_RESULT", 0) ?: 0
            sumState = sum;
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "The activity returned with sum: $sumState", Toast.LENGTH_LONG).show()
            }
            if(sumState > 10){
                val intent = Intent(this, Colocviu1_2Service::class.java)
                intent.putExtra(SUM_RESULT_STATE, sumState)
                startService(intent)
                serviceStatus = SERVICE_STARTED

            }
        }

        computeButton.setOnClickListener {
            if(oldAllTerms == allTermViewText.text.toString()){
                Toast.makeText(this, "The activity returned with sum(no calc): $sumState", Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(this, Colocviu1_2SecondaryActivity::class.java)
                intent.putExtra(All_TERMS_STATE, allTermViewText.text.toString())
                activityResultsLauncher.launch(intent)
                oldAllTerms = allTermViewText.text.toString()
            }

        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(SUM_RESULT_STATE, sumState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(SUM_RESULT_STATE)) {
            sumState = savedInstanceState.getInt(SUM_RESULT_STATE)
        } else {
            sumState = 0
        }
        Toast.makeText(this, "The activity returned with sum: $sumState", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        unregisterReceiver(messageBroadcastReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        val intent = Intent(
            this,
            Colocviu1_2Service::class.java
        )
        stopService(intent)
        super.onDestroy()
    }
}