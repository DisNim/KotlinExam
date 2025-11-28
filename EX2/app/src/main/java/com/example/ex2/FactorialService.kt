package com.example.ex2

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FactorialService : Service() {
    private var callBack: (Int) -> Unit = {}
    private var isRun = true

    inner class FactorialBinder: Binder(){
        fun setCallback(cb: (Int) -> Unit){
            callBack = cb
        }
    }

    private var binder = FactorialBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var num = 0
        CoroutineScope(Dispatchers.Default).launch {
            while (isRun){
                if (num >= Long.MAX_VALUE){
                    isRun = false
                    break
                }
                delay(500)
                val f = findFactorial(num)
                callBack(f)
                num+=1
            }
        }
        return START_STICKY
    }

    fun findFactorial(num: Int): Int {
        if (num <= 1)
            return 1
        var f = 1
        for (i in 1..num){
            f *= i
        }
        return f
    }
    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        isRun = false
        super.onDestroy()
    }
}