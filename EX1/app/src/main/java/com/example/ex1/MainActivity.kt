package com.example.ex1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

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
fun View(){
    var isStarted by remember{mutableStateOf(false)}
    val context = LocalContext.current
    var calculationStatus by remember { mutableStateOf("Готов к вычислениям") }
    var fibonacciResults by remember {mutableStateOf<List<FibonacciItem>>(emptyList())}

    val intent = remember { Intent(context, FibonacciService::class.java).apply {
        action = FibonacciService.ACTION_START_CALCULATION
        putExtra(FibonacciService.EXTRA_MAX_NUMBER, 10000)
    }}
    val stopIntent = remember{Intent(context, FibonacciService::class.java).apply {
        action = FibonacciService.ACTION_STOP_CALCULATION
    }}

    val fibonacciReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.getStringExtra("action")) {
                    "STARTED" -> {
                        if (!isStarted) return
                        calculationStatus = "Вычисления начаты"
                    }
                    "CALCULATION" -> {
                        val n = intent.getIntExtra("number", 0)
                        val res = intent.getStringExtra("result") ?: return

                        fibonacciResults = fibonacciResults + FibonacciItem(n, res)
                        calculationStatus = "Вычисление чисел: ${fibonacciResults.size}"
                    }
                    "COMPLETED" -> {
                        calculationStatus = "Вычисления завершены! Всего чисел: ${fibonacciResults.size}"
                        isStarted = false
                    }
                    "STOPPED" -> {
                        calculationStatus = "Вычисления остановлены. Вычислено чисел: ${fibonacciResults.size}"
                        isStarted = false
                    }
                    "ERROR" -> {
                        val message = intent.getStringExtra("message") ?: "Неизвестная ошибка"
                        calculationStatus = "Ошибка: $message"
                        isStarted = false
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        val filter = IntentFilter("FIBONACCI_ACTION")
        ContextCompat.registerReceiver(
            context,
            fibonacciReceiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
        onDispose { context.unregisterReceiver(fibonacciReceiver) }
    }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(text="Сатус вычисления: ${calculationStatus}")
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            if (!isStarted){
                fibonacciResults = emptyList()
                context.startService(intent)
                isStarted = true
            }
            else {
                context.stopService(stopIntent)
                isStarted = false
            }
        }) { Text(text = if (!isStarted) "Начать вычисления" else "Остановить высичления")}
        Spacer(modifier = Modifier.height(30.dp))
        Text("Результаты появятся здесь")
        LazyRow {
            items(items = fibonacciResults, key = {it.number}){item -> Text("F(${item.number}) = ${item.result} ")}
        }
    }
}

data class FibonacciItem(val number: Int, val result: String)