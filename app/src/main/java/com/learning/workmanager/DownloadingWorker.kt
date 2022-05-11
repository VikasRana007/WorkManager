package com.learning.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*


class DownloadingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    @SuppressLint("SimpleDateFormat")
    override fun doWork(): Result {
        return try {
            for (i: Int in 0..3000) {
                Log.i("MYTAG", "Downloading $i")
            }
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())
            Log.i("MyTAG","Completed $currentDate")

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}