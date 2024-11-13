package ro.pub.cs.systems.eim.colocviu1_2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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

class Colocviu1_2MainActivity : AppCompatActivity() {
    lateinit var nextTermEditText: EditText
    lateinit var allTermViewText: TextView
    lateinit var addButton: Button
    lateinit var computeButton: Button

    private val buttonClickListener = ButtonClickListener()

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val nextTermEdit = nextTermEditText.text.toString()
            var allTermView = allTermViewText.text.toString()
            when (view.id) {
                R.id.AddButton -> {
                    if(nextTermEdit != ""){
                        allTermView += " + $nextTermEdit"
                        allTermViewText.setText(allTermView)
                    }
                }
            }
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
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "The activity returned with sum: $sum", Toast.LENGTH_LONG).show()
            }
        }

        computeButton.setOnClickListener {
            val intent = Intent(this, Colocviu1_2SecondaryActivity::class.java)
            intent.putExtra(All_TERMS_STATE, allTermViewText.text.toString())
            activityResultsLauncher.launch(intent)
        }
    }
}