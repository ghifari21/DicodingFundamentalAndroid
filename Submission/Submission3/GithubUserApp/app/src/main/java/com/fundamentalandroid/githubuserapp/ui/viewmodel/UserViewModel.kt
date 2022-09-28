package com.fundamentalandroid.githubuserapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fundamentalandroid.githubuserapp.data.UserRepository
import com.fundamentalandroid.githubuserapp.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSearch(username: String) = userRepository.getSearch(username)

    fun getUserDetail(username: String) = userRepository.getUserDetail(username)

    fun getFollow(username: String, type: String) = userRepository.getFollowData(username, type)

    fun getUserFavorite() = userRepository.getUserFavorite()

    fun setUserFavorite(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setUserFavorite(user)
        }
    }
}