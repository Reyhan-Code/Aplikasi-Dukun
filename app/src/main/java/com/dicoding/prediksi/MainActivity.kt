package com.dicoding.prediksi

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextData: EditText
    private lateinit var buttonPredict: Button
    private lateinit var textViewPrediction: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton3Months: RadioButton
    private lateinit var radioButton4Months: RadioButton
    private lateinit var radioButton5Months: RadioButton

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextData = findViewById(R.id.editTextData)
        buttonPredict = findViewById(R.id.buttonPredict)
        textViewPrediction = findViewById(R.id.textViewPrediction)
        radioGroup = findViewById(R.id.radioGroup)
        radioButton3Months = findViewById(R.id.radioButton3Months)
        radioButton4Months = findViewById(R.id.radioButton4Months)
        radioButton5Months = findViewById(R.id.radioButton5Months)

        buttonPredict.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            val inputData = parseInputData()
            if (inputData.isNotEmpty()) {
                val windowSize = getSelectedWindowSize(selectedRadioButtonId)
                if (inputData.size >= windowSize) {
                    val predictions = movingAverage(inputData, windowSize)
                    val lastPrediction = predictions.lastOrNull()
                    val roundedPrediction = lastPrediction?.let { String.format("%.2f", it) }
                    textViewPrediction.text = "Hasil Prediksi: ${roundedPrediction ?: "N/A"}"
                } else {
                    textViewPrediction.text = "Data input tidak cukup"
                }
            } else {
                textViewPrediction.text = "Data input kosong"
            }
        }
    }

    // Mengambil data dan memisahkan dengan tanda koma (,)
    private fun parseInputData(): List<Double> {
        val input = editTextData.text.toString().trim()
        return if (input.isNotBlank()) {
            input.split(",").mapNotNull { it.toDoubleOrNull() }
        } else {
            emptyList()
        }
    }

    // Fungsi untuk menghitung rata-rata menggunakan MA
    private fun movingAverage(data: List<Double>, windowSize: Int): List<Double> {
        val predictions = mutableListOf<Double>()

        for (i in windowSize - 1 until data.size) {
            val window = data.subList(i - windowSize + 1, i + 1)
            val sum = window.sum()
            val average = sum / windowSize.toDouble()
            predictions.add(average)
        }

        return predictions
    }

    // Mendapatkan ukuran jendela yang dipilih berdasarkan ID RadioButton terpilih
    private fun getSelectedWindowSize(selectedRadioButtonId: Int): Int {
        return when (selectedRadioButtonId) {
            radioButton3Months.id -> 3
            radioButton4Months.id -> 4
            radioButton5Months.id -> 5
            else -> 3 // Default: menggunakan data 3 bulan terakhir jika tidak ada yang terpilih
        }
    }
}