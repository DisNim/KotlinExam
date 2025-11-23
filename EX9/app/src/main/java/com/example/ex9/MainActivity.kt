package com.example.ex9

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ex9.ui.theme.EX9Theme

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
    var numbers by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(setOf<Int>()) }
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        OutlinedTextField(value = numbers, onValueChange = {numbers = it},
            placeholder = {Text("Числа")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick={
            if (!numbers.isEmpty()){
                result = findNumbers(numbers)
            }
        }) {Text("Найти числа")}
        Spacer(modifier=Modifier.height(30.dp))
        Text("Результат", fontSize = 18.sp)
        LazyRow{
            items(items=result.toList()){r -> Text("${r}")}
        }
    }
}

fun findNumbers(numbers: String): Set<Int> {
    Log.d("TT", "OK")
    var listNumbers = numbers.split(' ')
    return listNumbers.map{number -> number.toSet().map{it.toString().toInt()}.toSet()}.reduce{acc, digits -> acc.intersect(digits)}
}
@Preview(showBackground = true)
@Composable
fun ViewPreview() {
    View()
}