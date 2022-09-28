package com.fundamentalandroid.mybroadcastreceiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.fundamentalandroid.mybroadcastreceiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var downloadReceiver: BroadcastReceiver

    var requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            Toast.makeText(this, "Sms receiver permission diterima", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Sms receiver permission ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPermission.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
        }
        binding.btnDownload.setOnClickListener {
            val downloadServiceIntent = Intent(this, DownloadService::class.java)
            startService(downloadServiceIntent)
        }

        downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d(DownloadService.TAG, "Download Selesai")
                Toast.makeText(context, "Download Selesai", Toast.LENGTH_SHORT).show()
            }
        }
        val downloadIntentFilter = IntentFilter(ACTION_DOWNLOAD_STATUS)
        registerReceiver(downloadReceiver, downloadIntentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadReceiver)
    }

    companion object {
        const val ACTION_DOWNLOAD_STATUS = "download_status"
        private const val SMS_REQUEST_CODE = 101
    }
}