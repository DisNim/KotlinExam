package com.example.ex5

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.CalculationButton).setOnClickListener {
            val a = findViewById<EditText>(R.id.AEnterBox).text.toString().toDouble()
            val b = findViewById<EditText>(R.id.BEnterBox).text.toString().toDouble()
            val c = findViewById<EditText>(R.id.CEnterBox).text.toString().toDouble()
            val D = calculateD(a,b,c)
            val res = findArguments(a,b,D)
            try{
                if (res.x2 == 0.0){
                    findViewById<TextView>(R.id.ResultText).text = "Результат: x = ${res.x1}"
                }
                else {
                    findViewById<TextView>(R.id.ResultText).text =
                        "Результат: x1 = ${res.x1} x2 = ${res.x2}"
                }
            } catch (e: IllegalArgumentException){
                findViewById<TextView>(R.id.ResultText).text = "Ошибка: ${e.message}"
            }

        }


    }
    fun findArguments(a:Double, b:Double, D:Double):Results {
        var x1 = 0.0
        var x2 = 0.0
        if (D < 0)
            throw IllegalArgumentException("Уравнение не имеет карней")
        if (D == 0.0){
            x1 = -b / 2*a
        }
        if (D > 0.0){
            x1 = (-b - Math.sqrt(D)) / (2 * a)
            x2 = (-b + Math.sqrt(D)) / (2 * a)
        }
        return Results(x1 = x1, x2 = x2)
    }
    fun calculateD(a:Double, b:Double, c:Double): Double{
        var D = 0.0
        if (a <= 0){
            throw IllegalArgumentException("'a' должна быть больше 0")
        }
        D = Math.pow(b, 2.0) - 4 * a * c
        return D
    }
}

data class Results(val x1: Double, val x2: Double)