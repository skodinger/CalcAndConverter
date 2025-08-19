package com.example.calcandconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private var input = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Link to activity_main.xml

        tvResult = findViewById(R.id.tvResult)

        // === CALCULATOR BUTTONS ===
        val buttons = listOf(
            Pair(R.id.btn0, "0"), Pair(R.id.btn1, "1"), Pair(R.id.btn2, "2"),
            Pair(R.id.btn3, "3"), Pair(R.id.btn4, "4"), Pair(R.id.btn5, "5"),
            Pair(R.id.btn6, "6"), Pair(R.id.btn7, "7"), Pair(R.id.btn8, "8"),
            Pair(R.id.btn9, "9"), Pair(R.id.btnPlus, "+"), Pair(R.id.btnMinus, "-"),
            Pair(R.id.btnMultiply, "*"), Pair(R.id.btnDivide, "/"), Pair(R.id.btnDot, ".")
        )

        for ((id, value) in buttons) {
            findViewById<Button>(id).setOnClickListener {
                input += value
                tvResult.text = input
            }
        }

        // Clear button: reset input
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            input = ""
            tvResult.text = getString(R.string.result) // back to 0
        }


        // Equal button: evaluate expression with Exp4j
        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            try {
                if (input.isNotEmpty()) { // Check if there's input to evaluate
                    val result = ExpressionBuilder(input).build().evaluate()
                    tvResult.text = result.toString()
                    input = result.toString()
                }
            } catch (e: Exception) {
                tvResult.text = getString(R.string.error)
                input = ""
            }
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            if (input.isNotEmpty()) {
                input = input.dropLast(1)
                tvResult.text = input
            }
        }

        findViewById<Button>(R.id.btnPercent).setOnClickListener {
            try {
                val currentValue = input.toDouble()
                val result = currentValue / 100.0
                tvResult.text = result.toString()
                input = result.toString()
            } catch (e: Exception) {
                // Handle cases where the input is not a valid number
                tvResult.text = "Error"
            }
        }

        // === CONVERSION FEATURE ===
        val etInput = findViewById<EditText>(R.id.etInput)
        val spinner = findViewById<Spinner>(R.id.spinnerConversion)
        val btnConvert = findViewById<Button>(R.id.btnConvert)
        val tvConversionResult = findViewById<TextView>(R.id.tvConversionResult)

        // Dropdown options
        val conversions = arrayOf(
            getString(R.string.inches_to_cm),
            getString(R.string.cm_to_inches),
            getString(R.string.pounds_to_kg),
            getString(R.string.kg_to_pounds),
            getString(R.string.fahrenheit_to_celsius),
            getString(R.string.celsius_to_fahrenheit),
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, conversions)
        spinner.adapter = adapter

        // Convert button logic
        btnConvert.setOnClickListener {
            val value = etInput.text.toString().toDoubleOrNull()
            if (value != null) {
                val result = when (spinner.selectedItem.toString()) {
                    getString(R.string.inches_to_cm) -> value * 2.54
                    getString(R.string.cm_to_inches) -> value * 0.393701
                    getString(R.string.pounds_to_kg) -> value * 0.453592
                    getString(R.string.kg_to_pounds) -> value * 2.20462
                    getString(R.string.fahrenheit_to_celsius) -> (value - 32) * 5/9
                    getString(R.string.celsius_to_fahrenheit) -> (value * 9/5) + 32
                    else -> 0.0
                }
                // For calculator errors
                tvResult.text = getString(R.string.error)

// For invalid conversion input
                tvConversionResult.text = getString(R.string.enter_valid_number)

// For valid conversion
                tvConversionResult.text = getString(R.string.conversion_result, result.toString())

            } else {
                tvConversionResult.text = getString(R.string.enter_valid_number)
            }
        }
    }
}
