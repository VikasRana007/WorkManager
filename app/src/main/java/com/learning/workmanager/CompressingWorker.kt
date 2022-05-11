package com.learning.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class CompressingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        return try {
            for (i: Int in 0..300) {
                Log.i("MYTAG", "Compressing $i")
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}