package com.example.ex1

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FibonacciService : Service() {
    private val binder = FibonacciBinder()
    var callback: (Int) -> Unit = {}
    private var isRun = true

    inner class FibonacciBinder: Binder(){
        fun setCallback(cb: (Int) -> Unit){
            callback = cb
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.Default).launch {
            var seper1 = 0
            var seper2 = 1
            var sum = 0

            while (isRun) {
                if (sum > Int.MAX_VALUE){
                    isRun = false
                    break
                }
                sum = seper1 + seper2
                seper1 = seper2
                seper2 = sum
                delay(500)
                callback(sum)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        isRun = false
        super.onDestroy()
    }
    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}