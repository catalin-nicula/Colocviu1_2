package ro.pub.cs.systems.eim.colocviu1_2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ro.pub.cs.systems.eim.colocviu1_2.Constants.SUM_RESULT

class Colocviu1_2SecondaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val input = intent.getStringExtra(Constants.All_TERMS_STATE) ?: ""
        val sum = calculateSumFromString(input)
        val resultIntent = Intent()
        resultIntent.putExtra(SUM_RESULT, sum)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun calculateSumFromString(input: String): Int {
        val numbers = input.split(" + ") // Split the string by the delimiter
        var sum = 0
        for (numberString in numbers) {
            try {
                sum += numberString.toInt() // Convert each part to an Int and add to sum
            } catch (e: NumberFormatException) {
                // Handle potential NumberFormatException (e.g., if a part isn't a number)
                // You might log the error, return a default value, or throw an exception
                Log.e("CalculateSum", "Invalid number format: $numberString")
            }
        }
        return sum
    }
}