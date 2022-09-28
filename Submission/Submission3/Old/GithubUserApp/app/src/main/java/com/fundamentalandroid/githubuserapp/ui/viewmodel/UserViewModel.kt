package com.fundamentalandroid.githubuserapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.fundamentalandroid.githubuserapp.data.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserList(username: String) = userRepository.getUserList(username)
}