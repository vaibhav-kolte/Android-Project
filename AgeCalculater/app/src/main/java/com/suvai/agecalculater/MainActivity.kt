package com.suvai.agecalculater

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var tvTodayDate: TextView
    private lateinit var tvDateOfBirth: TextView
    private lateinit var tvResult: TextView
    private lateinit var btnCalculateAge: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentDate: LocalDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val formattedDate: String = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        tvTodayDate = findViewById(R.id.tvTodayDate)
        tvDateOfBirth = findViewById(R.id.tvDateOfBirth)
        tvResult = findViewById(R.id.tvResult)
        btnCalculateAge = findViewById(R.id.btnCalculateAge)
        tvTodayDate.text = formattedDate

        tvDateOfBirth.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this, { _, year, monthOfYear, dayOfMonth ->
                    tvDateOfBirth.text = "$year-${String.format("%02d", (monthOfYear + 1))}-${
                        String.format(
                            "%02d",
                            dayOfMonth
                        )
                    }"
                },
                year, month, day
            )
            datePickerDialog.datePicker.maxDate = c.timeInMillis
            datePickerDialog.show()
        }
        tvTodayDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this, { _, year, monthOfYear, dayOfMonth ->
                    tvTodayDate.text = "$year-${String.format("%02d", (monthOfYear + 1))}-${
                        String.format(
                            "%02d",
                            dayOfMonth
                        )
                    }"
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        btnCalculateAge.setOnClickListener {
            val todayDate = LocalDate.parse(tvTodayDate.text, DateTimeFormatter.ISO_LOCAL_DATE)
            val dob = LocalDate.parse(tvDateOfBirth.text, DateTimeFormatter.ISO_LOCAL_DATE)

            val todayDay = todayDate.dayOfMonth
            val todayMonth = todayDate.monthValue
            val todayYear = todayDate.year

            val dobDay = dob.dayOfMonth
            var dobMonth = dob.monthValue
            var dobYear = dob.year

            var day = todayDay - dobDay
            if (day < 0) {
                val month = todayMonth - 1
                dobMonth += 1
                day += when (month) {
                    4, 6, 9, 11 -> 30
                    2 -> if ((todayYear % 4 == 0 && todayYear % 100 != 0) || todayYear % 400 == 0) 29 else 28
                    else -> 31
                }
            }
            var month = todayMonth - dobMonth
            if (month < 0) {
                dobYear += 1
                month += 12
            }

            val year = todayYear - dobYear
            tvResult.text = "You are now $year year:$month month:$day days old"
            Log.e(Companion.TAG, "onCreate: $todayDate")

        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}