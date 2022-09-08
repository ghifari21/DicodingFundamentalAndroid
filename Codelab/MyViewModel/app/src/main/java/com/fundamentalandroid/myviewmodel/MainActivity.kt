package com.fundamentalandroid.myviewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.fundamentalandroid.myviewmodel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var viewModel: MainViewModel
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        displayResult()

        binding.apply {
            btnCalculate.setOnClickListener {
                val width = edtWidth.text.toString()
                val height = edtHeight.text.toString()
                val length = edtLength.text.toString()

                when {
                    width.isEmpty() -> {
                        edtWidth.error = "Masih kosong"
                    }
                    height.isEmpty() -> {
                        edtHeight.error = "Masih kosong"
                    }
                    length.isEmpty() -> {
                        edtLength.error = "Masih kosong"
                    }
                    else -> {
                        viewModel.calculate(width, height, length)
                        displayResult()
                    }
                }
            }
        }
    }

    private fun displayResult() {
        binding.tvResult.text = viewModel.result.toString()
    }
}