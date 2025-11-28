package com.example.ex2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ex2.ui.theme.EX2Theme

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

    var isStart by remember { mutableStateOf(false) }
    var result by remember {mutableStateOf("Результат будет здесь")}
    val serviceConnection = remember {object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as FactorialService.FactorialBinder
            binder.setCallback { number -> result = number.toString()}
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }}
    val context = LocalContext.current
    val intent = Intent(context, FactorialService::class.java)

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(text=result, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            if (!isStart){
                context.startService(intent)
                context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            }
            else{
                context.stopService(intent)
                context.unbindService(serviceConnection)
            }
            isStart = !isStart
        }) {
            Text(text=if (!isStart) "Начать расчет" else "Остановить")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewPreview() {

}