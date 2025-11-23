package com.example.ex3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.contentType
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ex3.ui.theme.EX3Theme
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            View()
        }
    }
}

@Composable
fun View() {
    var number by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        OutlinedTextField(value = number, onValueChange = {number = it},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(35.dp))
        Button(onClick = {
            if (!number.isEmpty()){
                result = calculateFibonacci(number.toDouble())
            }
            else{
                result = "Пустое поле ввода"
            }
        }) { Text("Найти число по номеру")}
        Spacer(modifier = Modifier.height(35.dp))
        Text("Результат", fontSize = 18.sp)
        Text(text = result, fontSize = 16.sp)
    }
}


fun calculateFibonacci(number: Double): String {
    val leftPart = Math.pow((1+Math.sqrt(5.0)) / 2, number)
    val rightPart = Math.pow((1-Math.sqrt(5.0)) / 2, number)
    val F = (leftPart - rightPart) / Math.sqrt(5.0)
    return F.roundToLong().toString()
}
@Preview(showBackground = true)
@Composable
fun ViewPreview() {
    View()
}