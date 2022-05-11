package com.learning.workmanager

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.learning.workmanager.UploadWorker.Companion.KEY_WORKER
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_VALUE_COUNT = "key_value"
    }

    private lateinit var button: Button
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.start_button)
        textView = findViewById(R.id.textView)
        button.setOnClickListener {
//            setOneTimeWorkRequest() For Parallel and sequential one time request function
            setPeriodicWorkRequest()   // periodic time request function
        }
    }

    // In this function we write codes to tell work manager to perform the task our task is a one time work request
    private fun setOneTimeWorkRequest() {
        val workManager = WorkManager.getInstance(applicationContext)

        // This Data object is required to set I/O data with work manager
        val data = Data.Builder()
            .putInt(KEY_VALUE_COUNT, 125)
            .build()

        val constraint = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(UploadWorker::class.java)
                .setConstraints(constraint)
                .setInputData(data)
                .build()


        val filteringRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(FilteringWorker::class.java)
                .build()

        val compressingWorker: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(CompressingWorker::class.java)
                .build()

        val downloadingWorker : OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(DownloadingWorker::class.java)
                .build()
        // when we chaining parallel workers , first we need to add them to a mutable list,
        // Let's create a MutableList instance of one time work request
        val parallelWorks : MutableList<OneTimeWorkRequest> = mutableListOf()
        //code to observe the WorkInfo object   as well as  // This is how chaining workers  // this is called sequential chaining

        parallelWorks.add(downloadingWorker)
        parallelWorks.add(filteringRequest)

        workManager.beginWith(parallelWorks)
            .then(compressingWorker)
            .then(uploadRequest)
            .enqueue()

        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this) {
                textView.text = it.state.name
                if (it.state.isFinished) {
                    val data: Data = it.outputData
                    val message = data.getString(KEY_WORKER)
                    Toast.makeText(this, "$message", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun setPeriodicWorkRequest(){
        val periodicWorkRequest = PeriodicWorkRequest.Builder(DownloadingWorker::class.java,16,TimeUnit.MINUTES)  // two extra parameter in this builder repeat interval and time unit of repeat interval
            .build()
        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)

    }
}