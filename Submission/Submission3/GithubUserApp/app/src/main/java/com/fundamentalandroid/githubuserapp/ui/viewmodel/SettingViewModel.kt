package com.fundamentalandroid.githubuserapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fundamentalandroid.githubuserapp.data.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSetting() = userRepository.getTheme().asLiveData()

    fun setSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            userRepository.saveTheme(isDarkModeActive)
        }
    }
}