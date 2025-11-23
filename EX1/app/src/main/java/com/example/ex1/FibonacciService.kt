package com.example.ex1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigInteger


class FibonacciService: Service(){

    companion object {
        const val ACTION_START_CALCULATION = "START_CALCULATION"
        const val ACTION_STOP_CALCULATION = "STOP_CALCULATION"
        const val EXTRA_MAX_NUMBER = "max_number"
        const val TAG = "FibonacciService"
    }

    private var calculationJob: Job? = null
    private var isCalculation = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action){
            ACTION_START_CALCULATION -> {
                val maxNumber = intent.getIntExtra(EXTRA_MAX_NUMBER, 10000)
                startFibonacciCalculation(maxNumber)
            }
            ACTION_STOP_CALCULATION ->{
                stopFibonacciCalculation()

            }
        }
        return START_STICKY
    }
    private fun startFibonacciCalculation(maxNumber: Int) {
        if (isCalculation){
            Log.d(TAG, "Calculation already in progress")
            return
        }
        isCalculation = true
        calculationJob = CoroutineScope(Dispatchers.Default).launch{
            try{
                calculateFibonacciSequence(maxNumber)
            }
            catch (e: Exception){
                Log.e(TAG, "Error during Fibonacci calculation", e)
                sendBroadcast(createResultIntent("STARTED", "Calculation started for 1..$maxNumber"))
            }
        }
    }
    private fun stopFibonacciCalculation() {
        calculationJob?.cancel()
        calculationJob = null
        isCalculation = false
        Log.d(TAG, "Fibonacci calculation stopped")
        sendBroadcast(createResultIntent("STOPPED", "Calculation stopped"))
    }
    private suspend fun calculateFibonacciSequence(maxNumber: Int) {
        if (maxNumber < 1){
            sendBroadcast(createResultIntent("ERROR", "Invalid max number: $maxNumber"))
            return
        }
        var a = BigInteger.ZERO
        var b = BigInteger.ONE

        if (maxNumber == 0){
            sendFibonacciResult(0, BigInteger.ZERO)
        }
        if (maxNumber == 1){
            sendFibonacciResult(1, BigInteger.ONE)
        }
        for (i in 2..maxNumber){
            if (!isCalculation) break
            val next = a + b
            a = b
            b = next
            sendFibonacciResult(i, next)
            delay(50)
        }
        if (isCalculation){
            sendBroadcast(createResultIntent("COMPLETED", "Calculation completed for 1..$maxNumber"))
            Log.d(TAG, "Fibonacci calculation completed")
        }
    }

    private fun sendFibonacciResult(n: Int, result: BigInteger) {
        Log.d(TAG, "F($n) = $result")
        val intent = createResultIntent("CALCULATION", "F($n) calculated")
        intent.putExtra("number", n)
        intent.putExtra("result", result.toString())
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        calculationJob?.cancel()
        isCalculation = false
        super.onDestroy()
    }
    private fun createResultIntent(action: String, message: String): Intent {
        return Intent("FIBONACCI_ACTION").apply {
            putExtra("action", action)
            putExtra("message", message)
            putExtra("timestamp", System.currentTimeMillis())
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}