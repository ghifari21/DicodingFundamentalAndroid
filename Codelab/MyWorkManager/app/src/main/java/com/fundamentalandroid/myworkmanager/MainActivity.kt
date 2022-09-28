package com.fundamentalandroid.myworkmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.fundamentalandroid.myworkmanager.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var periodicWorkRequest: PeriodicWorkRequest
    private lateinit var binding: ActivityMainBinding
    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workManager = WorkManager.getInstance(this)

        binding.btnOneTimeTask.setOnClickListener {
            startOneTimeTask()
        }
        binding.btnPeriodicTask.setOnClickListener {
            startPeriodicTask()
        }
        binding.btnCancelTask.setOnClickListener {
            cancelPeriodicTask()
        }
    }

    private fun startOneTimeTask() {
        binding.textStatus.text = getString(R.string.status)
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, binding.editCity.text.toString())
            .build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(oneTimeWorkRequest)
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this@MainActivity) {
                val status = it.state.name
                binding.textStatus.append("\n" + status)
            }
    }

    private fun startPeriodicTask() {
        binding.textStatus.text = getString(R.string.status)
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, binding.editCity.text.toString())
            .build()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        workManager.enqueue(periodicWorkRequest)
        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this@MainActivity) {
                val status = it.state.name
                binding.textStatus.append("\n" + status)
                binding.btnCancelTask.isEnabled = false
                if (it.state == WorkInfo.State.ENQUEUED) binding.btnCancelTask.isEnabled = true
            }
    }

    private fun cancelPeriodicTask() {
        workManager.cancelWorkById(periodicWorkRequest.id)
    }
}