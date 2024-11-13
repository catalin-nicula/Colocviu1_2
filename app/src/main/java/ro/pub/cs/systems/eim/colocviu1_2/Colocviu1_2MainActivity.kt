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
import ro.pub.cs.systems.eim.colocviu1_2.Constants.SUM_RESULT_STATE

class Colocviu1_2MainActivity : AppCompatActivity() {
    lateinit var nextTermEditText: EditText
    lateinit var allTermViewText: TextView
    lateinit var addButton: Button
    lateinit var computeButton: Button
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
}