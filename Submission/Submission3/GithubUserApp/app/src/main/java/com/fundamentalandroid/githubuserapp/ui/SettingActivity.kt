package com.fundamentalandroid.githubuserapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.fundamentalandroid.githubuserapp.R
import com.fundamentalandroid.githubuserapp.databinding.ActivitySettingBinding
import com.fundamentalandroid.githubuserapp.ui.viewmodel.SettingViewModel
import com.fundamentalandroid.githubuserapp.ui.viewmodel.ViewModelFactory

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val viewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.setting)

        viewModel.getSetting().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.swDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.swDarkMode.isChecked = false
            }
        }

        binding.swDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.setSetting(isChecked)
        }
    }
}