package com.learning.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.learning.workmanager.MainActivity.Companion.KEY_VALUE_COUNT
import java.util.*

class UploadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    companion object {
        const val KEY_WORKER = "key_worker"
    }

    @SuppressLint("SimpleDateFormat")
    override fun doWork(): Result {
        return try {
            // To provide input data object work manager class has a getter function called input data
            val count = inputData.getInt(KEY_VALUE_COUNT, 0)
            for (i: Int in 0 until count) {
                Log.i("MYTAG", "Uploading $i")
            }
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())
            // create a data object to send the output data
            val outputData = Data.Builder()
                .putString(KEY_WORKER, currentDate)
                .build()
            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }
}