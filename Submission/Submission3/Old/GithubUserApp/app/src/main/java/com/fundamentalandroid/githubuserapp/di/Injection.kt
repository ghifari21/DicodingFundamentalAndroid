package com.fundamentalandroid.githubuserapp.di

import android.content.Context
import com.fundamentalandroid.githubuserapp.data.UserRepository
import com.fundamentalandroid.githubuserapp.data.local.room.UserDatabase
import com.fundamentalandroid.githubuserapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }
}